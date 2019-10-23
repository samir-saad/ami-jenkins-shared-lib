package org.ssaad.ami.pipeline.utils


import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.common.types.CreationPolicyType
import org.ssaad.ami.pipeline.common.types.DeploymentType
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.engine.OpenShiftDeploy
import org.ssaad.ami.pipeline.engine.OpenShiftS2I
import org.ssaad.ami.pipeline.platform.OpenShift
import org.ssaad.ami.pipeline.stage.PlatformStage
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl

class OpenShiftUtils implements Serializable {

    static void s2iBuild(OpenShiftS2I engine) {

        // Get objects
        Pipeline pipeline = PipelineRegistry.getPipeline(engine.buildId)
        Application app = pipeline.app
        PlatformStage stage = (PlatformStage) pipeline.findStage(engine.taskType)
        OpenShift platform = (OpenShift) stage.platform
        Deployment deployment = stage.deployment
//        List<Template> templates = stage.templates
        Template template = stage.template
        def steps = pipeline.steps
        Map bindings = ["platform": platform, "deployment": deployment, "engine": engine, "app": app, "stage": stage]

        // Prepare application package
        String appPakage = PipelineUtils.normalizePath(app.id + "/" + engine.appPackage)
        appPakage = PipelineUtils.resolveVars([app: app], appPakage)

        steps.println("Building container image for " + appPakage)

        steps.sh "rm -rf oc-build && mkdir -p oc-build/deployments"
        steps.sh "cp ${appPakage} oc-build/deployments/"

        steps.openshift.withProject(platform.project) {
            try {
                // Create build objects
                List templatesObjects = applyTemplate(pipeline, template, bindings, steps)

                // Get build config
                def bc = getResourceByKind(templatesObjects, "BuildConfig")
                steps.println("BC: ${bc}")
                def bcSelector = steps.openshift.selector(bc.kind, bc.metadata.name)
                steps.println("bcSelector: ${bcSelector}")
                steps.println("Starting a container build from ${bc.metadata.name}")

                // Create image push secret
                steps.println("Create image push secret ${engine.ocpSecretId} from ${engine.imagePushCredentialsId}")
                StringCredentialsImpl secret = (StringCredentialsImpl) JenkinsUtils.getCredentials(engine.imagePushCredentialsId)
                engine.imagePushSecretTemplate.params.put("TOKEN", secret.secret.value)
                applyTemplate(pipeline, engine.imagePushSecretTemplate, bindings, steps)

                // Start build
                bcSelector.startBuild("--from-dir=oc-build/deployments")

                // Get last build
                def lastBuildName = "${bcSelector.object().metadata.name}-${bcSelector.object().status.lastVersion}"
                def lastBuild = steps.openshift.selector('builds', lastBuildName)

                def goodBuildStatus = (String[]) ['Running', 'Pending', 'Complete']
                steps.timeout(time: stage.stageTimeout.time, unit: stage.stageTimeout.unit) {
                    lastBuild.untilEach(1) {
                        def status = it.object().status.phase
                        steps.println("Build status: ${status}")
                        if (!goodBuildStatus.contains(status)) {
                            throw new Exception("Build failed")
                        }
                        if ("Complete".equals(status)) {
                            steps.println("Build Completed")
                            return true
                        }
                    }
                }

                // Push image with commit tag
                String srcImage = PipelineUtils.resolveVars(bindings, template.params.get("IMAGE_NAME") + ":" + template.params.get("IMAGE_TAG"))
                String destImage = PipelineUtils.resolveVars(bindings, template.params.get("IMAGE_NAME") + ":" + app.latestCommit)
                String credentials = new String(secret.secret.value.decodeBase64())
                copyImage(steps, srcImage, destImage, credentials, credentials)

            } catch (Exception e) {
                e.printStackTrace()
                steps.println(e.getStackTrace())
            } finally {
                String imagePushSecretName = PipelineUtils.resolveVars(bindings, engine.imagePushSecretTemplate.params.get("OCP_OBJECT_NAME"))
                steps.println("Delete image push secret ${imagePushSecretName}")
                def secretSelector = steps.openshift.selector("Secret", imagePushSecretName)
                steps.println("secretSeclector: ${secretSelector}")
                secretSelector.delete()
            }
        }
    }

    static void copyImage(steps, srcImage, destImage, srcCredentials, destCredentials) {
        String imageCopyCommand = "skopeo copy docker://${srcImage} docker://${destImage} " +
                "--src-creds \"${srcCredentials}\" " +
                "--dest-creds \"${destCredentials}\" " +
                "--src-tls-verify=false --dest-tls-verify=false"

        steps.println("Image copy command:\n ${imageCopyCommand}")
        steps.sh(imageCopyCommand)
    }

    static void deploy(OpenShiftDeploy engine) {
        // Get objects
        Pipeline pipeline = PipelineRegistry.getPipeline(engine.buildId)
        Application app = pipeline.app
        PlatformStage stage = (PlatformStage) pipeline.findStage(engine.taskType)
        OpenShift platform = (OpenShift) stage.platform
        Deployment deployment = stage.deployment
//        List<Template> templates = stage.templates
        Template template = stage.template
        def steps = pipeline.steps

        // Check config
        checkConfig(steps, app, pipeline, stage, engine, platform, deployment, template)
        // Deploy
        deploy(steps, app, pipeline, stage, engine, platform, deployment, template)
        // Prepare test
        prepareTests(steps, app, pipeline, stage, engine, platform, deployment, template)
        // Perform tests
        performTests(steps, app, pipeline, stage, engine, platform, deployment, template)
        // Switch traffic
        switchTraffic(steps, app, pipeline, stage, engine, platform, deployment, template)
        // Approve release
        approveRelease(steps, app, pipeline, stage, engine, platform, deployment, template)

        // Push image with app version tag
        if(EnvironmentType.DEV.equals(deployment.environmentType)){
            steps.println("Pushing image with app version tag")
            Map bindings = ["platform": platform, "deployment": deployment, "engine": engine, "app": app, "stage": stage]
            StringCredentialsImpl secret = (StringCredentialsImpl) JenkinsUtils.getCredentials(engine.imagePushCredentialsId)

            String srcImage = PipelineUtils.resolveVars(bindings, template.params.get("IMAGE_NAME") + ":" + template.params.get("IMAGE_TAG"))
            String destImage = PipelineUtils.resolveVars(bindings, template.params.get("IMAGE_NAME") + ":" + app.version)
            String credentials = new String(secret.secret.value.decodeBase64())
            copyImage(steps, srcImage, destImage, credentials, credentials)
        }
    }

    static void checkConfig(steps, Application app, Pipeline pipeline, PlatformStage stage, OpenShiftDeploy engine,
                            OpenShift platform, Deployment deployment, Template template) {

        // Check configs for blue-green
        if (DeploymentType.BLUE_GREEN.equals(deployment.deploymentType)) {
            steps.println("checkConfig for BLUE_GREEN deployment")
            Map deployments = [:]

            steps.openshift.withProject(platform.project) {
                def dcSelector = steps.openshift.selector("dc", [app: app.id])
                def dcObject
                def deploymentTag
                dcSelector.withEach {
                    dcObject = it.object()
                    if (dcObject.metadata.labels != null) {
                        deploymentTag = dcObject.metadata.labels["deployment-tag"]
                    }
                    deployments.put(it.name(), deploymentTag)
                }
            }

            if (deployments.size() > 1) {
                steps.currentBuild.result = 'ABORTED'
                steps.error("More than one deployment found for app ${app.id}. Manual cleanup is required.")
            } else if (deployments.size() == 1) {
                String deploymentTag = deployments.get(deployments.keySet().first())
                if ("blue".equals(deploymentTag)) {
                    deployment.deploymentTag = "green"
                } else if ("green".equals(deploymentTag)) {
                    deployment.deploymentTag = "blue"
                } else {
                    steps.currentBuild.result = 'ABORTED'
                    steps.error("Deployment tag ${deploymentTag} is ivalid, expected values: [blue, green]. Manual cleanup is required.")
                }
            } else {
                deployment.deploymentTag = "blue"
            }

            if (!"".equals(deployment.deploymentTag?.trim())) {
                // Alter OCP_OBJECT_NAME
                String ocpObjectName = template.params.get("OCP_OBJECT_NAME") + "-" + deployment.deploymentTag
                template.params.put("OCP_OBJECT_NAME", ocpObjectName)
                deployment.autoScaling.autoScalingTemplate.params.put("OCP_OBJECT_NAME", ocpObjectName)
            }
        }
    }

    static void deploy(steps, Application app, Pipeline pipeline, PlatformStage stage, OpenShiftDeploy engine,
                       OpenShift platform, Deployment deployment, Template template) {
        steps.openshift.withProject(platform.project) {
            // Create deployment objects
            Map bindings = ["platform": platform, "deployment": deployment, "engine": engine, "app": app, "stage": stage]
            List templatesObjects = applyTemplate(pipeline, template, bindings, steps)

            // Get deployment config
            def dc = getResourceByKind(templatesObjects, "DeploymentConfig")
            steps.println("DC: ${dc}")
            def dcSelector = steps.openshift.selector(dc.kind, dc.metadata.name)
            steps.println("dcSelector: ${dcSelector}")

            // Add environment variables from secrets
            String appContainerName = PipelineUtils.resolveVars(bindings, template.params.get("OCP_OBJECT_NAME"))
            // Generic secret
            mountEnvVarsFromSecret(steps, platform, appContainerName, "application", dcSelector)
            // App-specific secret
            mountEnvVarsFromSecret(steps, platform, appContainerName, app.id, dcSelector)

            // Delete HPA
            if (deployment.autoScaling != null) {
                steps.println("Delete previously created HPA")
                deleteTemplate(pipeline, deployment.autoScaling.autoScalingTemplate, bindings, steps)
            }

            // Create image pull secret
            steps.println("Create image pull secret ${engine.ocpSecretId} from ${engine.imagePullCredentialsId}")
            StringCredentialsImpl secret = (StringCredentialsImpl) JenkinsUtils.getCredentials(engine.imagePullCredentialsId)
            engine.imagePullSecretTemplate.params.put("TOKEN", secret.secret.value)
            applyTemplate(pipeline, engine.imagePullSecretTemplate, bindings, steps)

            // Patch default SA to pull images
            String imagePullSecretName = PipelineUtils.resolveVars(bindings, engine.imagePullSecretTemplate.params.get("OCP_OBJECT_NAME"))
            String patch = '\'{"imagePullSecrets": [{"name":"' + imagePullSecretName + '"}]}\''
            def defaultSaSelector = steps.openshift.selector("ServiceAccount", "default")
            steps.println("Patch SA ${defaultSaSelector} :-> ${patch}")
            defaultSaSelector.patch(patch)

            // Wait for old pods to terminate
            steps.println("Waiting 10 seconds for old pods to terminate")
            steps.sleep(time: 10, unit: "SECONDS")

            // Start deployment
            steps.println("Starting deployment: ${dc.metadata.name}")
            dcSelector.rollout().latest()

            // Check pods readiness
            def currentDeployment = "${dc.metadata.name}-${dcSelector.object().status.latestVersion}"
            def pods = steps.openshift.selector('pods', [deployment: currentDeployment])

            steps.timeout(time: deployment.readinessTimeout.time, unit: deployment.readinessTimeout.unit) {
                pods.untilEach(deployment.replicas) {
                    steps.println("Pod ${it.object().metadata.name} is ${it.object().status.phase}. Checking containers readiness...")
                    return it.object().status.containerStatuses.every {
                        it.ready
                    }
                }
            }

            // Apply HPA
            if (deployment.autoScaling != null && deployment.autoScaling.enable) {
                steps.println("Apply HPA - waiting ${deployment.autoScaling.initialDelay.time} ${deployment.autoScaling.initialDelay.unit} for ${deployment.autoScaling.resourceType} to idel")
                // Initial delay
                steps.sleep(time: deployment.autoScaling.initialDelay.time, unit: deployment.autoScaling.initialDelay.unit)
                applyTemplate(pipeline, deployment.autoScaling.autoScalingTemplate, bindings, steps)
            }
        }
    }

    static void mountEnvVarsFromSecret(steps, OpenShift platform, String appContainerName, String secretName, dcSelector) {
        Map secretMap = getSecretContent(steps, secretName, platform)
        String patch
        for (String key : secretMap.keySet()) {
            patch = '\'{"spec":{"template":{"spec":{"containers":[{"name": "' + appContainerName + '", "env":[{"name": "' + key +
                    '", "valueFrom": {"secretKeyRef": {"key": "' + key + '", "name": "' + secretName + '"}}}]}]}}}}\''

            steps.println("Mount ${key}:-> ${patch}")
            dcSelector.patch(patch)
        }
    }

    static void prepareTests(steps, Application app, Pipeline pipeline, PlatformStage stage, OpenShiftDeploy engine,
                             OpenShift platform, Deployment deployment, Template template) {
        if (stage.testing?.isActive()) {
            steps.println("Testing is enabled - prepare tests")

            String soapuiProject = "${app.id}/${stage.testing.engine.configDir}/${app.id}-soapui-project.xml"
            String soapuiProperties = "${app.id}/${stage.testing.engine.configDir}/${deployment.environmentType.toString().toLowerCase()}.properties"

            steps.println("SOAPUI project file: ${soapuiProject}")
            steps.println("SOAPUI properties file: ${soapuiProperties}")

            if (steps.fileExists(soapuiProject) && steps.fileExists(soapuiProperties)) {
                steps.println("SOAPUI files exist, proceed with test preparation")
                // Load test properties
                Properties properties = steps.readProperties(file: soapuiProperties)
                steps.println("SOAPUI properties ${soapuiProperties}: \n ${properties}")

                // Set endpoint
                Map bindings = ["platform": platform, "deployment": deployment, "engine": engine, "app": app, "stage": stage]
                String routeName = PipelineUtils.resolveVars(bindings, template.params.get("OCP_OBJECT_NAME"))
                String host = getApplicationHost(steps, routeName, platform)
                properties.put("endpoint", host)

                Map secretMap = new HashMap()
                // Set app secrets
                secretMap.putAll(getSecretContent(steps, "application", platform))
                secretMap.putAll(getSecretContent(steps, "${app.id}", platform))

                // Set testing secrets
                secretMap.putAll(getSecretContent(steps, "application-testing", platform))
                secretMap.putAll(getSecretContent(steps, "${app.id}-testing", platform))

                properties.putAll(secretMap)

                // Prepare properties to be saved
                String propertiesString = ""
                for (String key : properties.keySet()) {
                    propertiesString += "${key}=${properties.getProperty(key)}\n"
                }
                steps.writeFile(file: soapuiProperties, text: propertiesString)

                steps.sh("cat ${soapuiProperties}")
            }
        }
    }

    static void performTests(steps, Application app, Pipeline pipeline, PlatformStage stage, OpenShiftDeploy engine,
                             OpenShift platform, Deployment deployment, Template template) {
        if (stage.testing?.isActive()) {
            steps.println("Testing is enabled - execute tests")

            String soapuiProject = "${app.id}/${stage.testing.engine.configDir}/${app.id}-soapui-project.xml"
            String soapuiProperties = "${app.id}/${stage.testing.engine.configDir}/${deployment.environmentType.toString().toLowerCase()}.properties"

            steps.println("SOAPUI project file: ${soapuiProject}")
            steps.println("SOAPUI properties file: ${soapuiProperties}")

            if (steps.fileExists(soapuiProject) && steps.fileExists(soapuiProperties)) {
                steps.println("SOAPUI files exist, proceed with test execution")
                stage.testing.execute()
            }
        }
    }

    static void switchTraffic(steps, Application app, Pipeline pipeline, PlatformStage stage, OpenShiftDeploy engine,
                              OpenShift platform, Deployment deployment, Template template) {

        if (DeploymentType.BLUE_GREEN.equals(deployment.deploymentType)) {
            steps.println("switchTraffic for BLUE_GREEN deployment")
            steps.stage("Switch Traffic") {
                steps.openshift.withProject(platform.project) {

                    Map bindings = ["platform": platform, "deployment": deployment, "engine": engine, "app": app, "stage": stage]

                    steps.timeout(time: stage.deployment.switchTrafficTimeout.time, unit: stage.deployment.switchTrafficTimeout.unit) {
                        def choices = steps.input(
                                message: "Switch traffic?",
                                ok: "OK",
                                parameters: [steps.choice(name: 'APPROVE', choices: 'confirm\nrollback', description: 'Switch Traffic?')]
                        )
                        if ("confirm".equals(choices)) {
                            List templateObjects = processTemplate(pipeline, template, bindings, steps)
                            templateObjects.each {
                                if ("Route".equalsIgnoreCase(it.kind)) {
                                    it.metadata.name = it.metadata.name.replace("-" + deployment.deploymentTag, "")
                                    createResource(it, CreationPolicyType.ENFORCE_RECREATE, steps)
                                }
                            }
                        } else {
                            // Rollback current deployment
                            steps.println("Rollback current deployment: ${deployment.deploymentTag}")
                            steps.println("Delete Main template objects")
                            deleteTemplate(pipeline, template, bindings, steps)

                            // Delete HPA
                            if (deployment.autoScaling != null) {
                                steps.println("Delete HPA")
                                deleteTemplate(pipeline, deployment.autoScaling.autoScalingTemplate, bindings, steps)
                            }
                            steps.currentBuild.result = 'ABORTED'
                            steps.error("Release rolled back by user")
                        }
                    }
                }
            }
        }
    }

    static String getAltTag(String tag) {
        if ("blue".equalsIgnoreCase(tag))
            return "green"
        else
            return "blue"
    }

    static void approveRelease(steps, Application app, Pipeline pipeline, PlatformStage stage, OpenShiftDeploy engine,
                               OpenShift platform, Deployment deployment, Template template) {

        if (DeploymentType.BLUE_GREEN.equals(deployment.deploymentType)) {
            steps.println("approveRelease for BLUE_GREEN deployment")
            steps.stage("Confirm Release") {
                steps.openshift.withProject(platform.project) {

                    String currentOcpObjectName = template.params.get("OCP_OBJECT_NAME")
                    String ocpObjectName = currentOcpObjectName.replace("-" + deployment.deploymentTag, "")
                    String oldOcpObjectName = ocpObjectName + "-" + getAltTag(deployment.deploymentTag)

                    steps.println("ocpObjectName: ${ocpObjectName}")
                    steps.println("currentOcpObjectName: ${currentOcpObjectName}")
                    steps.println("oldOcpObjectName: ${oldOcpObjectName}")

                    Map bindings = ["platform": platform, "deployment": deployment, "engine": engine, "app": app, "stage": stage]

                    steps.timeout(time: stage.deployment.releaseApprovalTimeout.time, unit: stage.deployment.releaseApprovalTimeout.unit) {
                        def choices = steps.input(
                                message: "Confirm Release?",
                                ok: "OK",
                                parameters: [steps.choice(name: 'APPROVE', choices: 'confirm\nrollback', description: 'Confirm Release?')]
                        )
                        if ("confirm".equals(choices)) {
                            steps.println("Release confirmed")
                            // Delete old deployment completely
                            steps.println("Delete old deployment completely")
                            template.params.put("OCP_OBJECT_NAME", oldOcpObjectName)
                            deleteTemplate(pipeline, template, bindings, steps)

                            // Delete old deployment HPA
                            steps.println("Delete old deployment HPA")
                            deployment.autoScaling.autoScalingTemplate.params.put("OCP_OBJECT_NAME", oldOcpObjectName)
                            deleteTemplate(pipeline, deployment.autoScaling.autoScalingTemplate, bindings, steps)

                            // Delete new deployment temp routes
                            steps.println("Delete new deployment temp routes")
                            template.params.put("OCP_OBJECT_NAME", currentOcpObjectName)
                            List templateObjects = processTemplate(pipeline, template, bindings, steps)
                            templateObjects.each {
                                if ("Route".equalsIgnoreCase(it.kind)) {
                                    deleteResource(it, steps)
                                }
                            }
                        } else {
                            steps.println("Release rejected")
                            // Delete new deployment completely
                            steps.println("Delete new deployment completely")
                            template.params.put("OCP_OBJECT_NAME", currentOcpObjectName)
                            deleteTemplate(pipeline, template, bindings, steps)

                            // Delete new deployment HPA
                            steps.println("Delete new deployment HPA")
                            deployment.autoScaling.autoScalingTemplate.params.put("OCP_OBJECT_NAME", currentOcpObjectName)
                            deleteTemplate(pipeline, deployment.autoScaling.autoScalingTemplate, bindings, steps)

                            // Switch prod routes to old service
                            steps.println("Switch prod routes to old service")
                            template.params.put("OCP_OBJECT_NAME", oldOcpObjectName)
                            List templateObjects = processTemplate(pipeline, template, bindings, steps)
                            templateObjects.each {
                                if ("Route".equalsIgnoreCase(it.kind)) {
                                    it.metadata.name = it.metadata.name.replace("-" + getAltTag(deployment.deploymentTag), "")
                                    createResource(it, CreationPolicyType.ENFORCE_RECREATE, steps)
                                }
                            }

                            steps.currentBuild.result = 'ABORTED'
                            steps.error("Release rolled back by user")
                        }

                        // Reset params
                        template.params.put("OCP_OBJECT_NAME", currentOcpObjectName)
                        deployment.autoScaling.autoScalingTemplate.params.put("OCP_OBJECT_NAME", currentOcpObjectName)
                    }
                }
            }
        }
    }

    static List applyTemplates(Pipeline pipeline, List<Template> templates, Map bindings, steps) {
        List templatesObjects = new ArrayList()
        for (Template template : templates) {
            templatesObjects.addAll(applyTemplate(pipeline, template, bindings, steps))
        }
        return templatesObjects
    }

    static List applyTemplate(Pipeline pipeline, Template template, Map bindings, steps) {

        steps.println("Apply template: ${template.name} with policy: ${template.creationPolicy}")
        List templateObjects = processTemplate(pipeline, template, bindings, steps)
        steps.println("templateObjects: ${templateObjects}")

        templateObjects.each {
            createResource(it, template.creationPolicy, steps)
        }

        return templateObjects
    }

    static List deleteTemplate(Pipeline pipeline, Template template, Map bindings, steps) {

        steps.println("Delete template: ${template.name}")
        List templateObjects = processTemplate(pipeline, template, bindings, steps)
        steps.println("templateObjects: ${templateObjects}")

        templateObjects.each {
            deleteResource(it, steps)
        }

        return templateObjects
    }

    private static List processTemplate(Pipeline pipeline, Template template, Map bindings, steps) {
        //Get config repo
        ScmRepository configRepo = PipelineUtils.findConfigRepo(pipeline, template.filePath)
        bindings.put("configRepo", configRepo)
        bindings.put("template", template)
        String templateFilePath = PipelineUtils.normalizePath(configRepo.localDir + "/" + template.filePath)

        resolveTemplateParams(template, bindings)
        resolveTemplateLabels(template, bindings)
        steps.println("Template params: ${template.parsedParams}")
        steps.println("Template labels: ${template.parsedLabels}")

        def templateContent = steps.readYaml file: templateFilePath
        List templateObjects = steps.openshift.process(templateContent, template.parsedParams, template.parsedLabels)
        return templateObjects
    }

    static void createResource(resource, CreationPolicyType creationPolicy, steps) {
        //Check if the resource exists
        def resourceSelector = steps.openshift.selector(resource.kind, resource.metadata.name)

        if (resourceSelector.exists()) {
            steps.println("${resource.kind} ${resource.metadata.name} exists")

            if (CreationPolicyType.CREATE_IF_NOT_EXIST.equals(creationPolicy)) {
                steps.println("Creation will be skipped for policy ${creationPolicy}")
                return
            } else if (CreationPolicyType.ENFORCE_RECREATE.equals(creationPolicy)) {
                steps.println("ReCreation will be enforced for policy ${creationPolicy}")
                steps.println("Deleting ${resource.kind} ${resource.metadata.name}")
                steps.openshift.delete(resource)
            }
        }
        steps.println("Creating ${resource.kind} ${resource.metadata.name}")
        steps.openshift.create(resource)
    }

    static void deleteResource(resource, steps) {
        //Check if the resource exists
        def resourceSelector = steps.openshift.selector(resource.kind, resource.metadata.name)

        if (resourceSelector.exists()) {
            steps.println("Deleting ${resource.kind} ${resource.metadata.name}")
            steps.openshift.delete(resource)
        }
    }

    static getResourceByKind(List resources, String kind) {
        def resource
        resources.each {
            if (kind.equals(it.kind))
                resource = it
        }
        return resource
    }

    static void resolveTemplateParams(Template template, Map bindings) {
        template.parsedParams = ""
        String paramValue
        if (template.params != null) {
            for (String param : template.params.keySet()) {
                paramValue = PipelineUtils.resolveVars(bindings, template.params.get(param))
                template.parsedParams += "-p ${param}=\'${paramValue}\' "
            }
        }
    }

    static void resolveTemplateLabels(Template template, Map bindings) {
        template.parsedLabels = ""
        if (template.labels != null && !template.labels.isEmpty()) {
            String parsedLabels = "-l "
            String labelValue
            for (String label : template.labels.keySet()) {
                labelValue = PipelineUtils.resolveVars(bindings, template.labels.get(label))
                parsedLabels += "${label}=\'${labelValue}\',"
            }
            template.parsedLabels = parsedLabels.substring(0, parsedLabels.length() - 1)
        }
    }

    static Map getSecretContent(steps, String secretName, OpenShift platform) {
        Map secretMap = new HashMap()
        steps.openshift.withProject(platform.project) {
            def secretSelector = steps.openshift.selector("Secret", secretName)
            steps.println("secretSelector: ${secretSelector}")
            if (secretSelector.exists()) {
                def secretObject = secretSelector.object()
                if (secretObject.data != null) {
                    Map secretData = secretObject.data
                    for (String key : secretData.keySet()) {
                        secretMap.put(key, new String(secretData.get(key).toString().decodeBase64()))
                    }
                }
            }
        }
        return secretMap
    }

    static String getApplicationHost(steps, String routeName, OpenShift platform) {
        String host
        steps.openshift.withProject(platform.project) {
            def routeSelector = steps.openshift.selector("Route", routeName)
            steps.println("routeSelector: ${routeSelector}")
            if (routeSelector.exists()) {
                if (routeSelector.object().spec.tls?.termination != null) {
                    host = "https://"
                } else {
                    host = "http://"
                }
                host += routeSelector.object().spec.host
            }
        }
        return host
    }

}
