package org.ssaad.ami.pipeline.utils

import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.common.types.CreationPolicyType
import org.ssaad.ami.pipeline.engine.OpenshiftS2I
import org.ssaad.ami.pipeline.platform.OpenShift
import org.ssaad.ami.pipeline.stage.PlatformStage

class OpenShiftUtils implements Serializable {

    static void build() {
    }

    static void s2iBuild(OpenshiftS2I engine) {

        // Get objects
        Pipeline pipeline = PipelineRegistry.getPipeline(engine.buildId)
        Application app = pipeline.app
        PlatformStage stage = (PlatformStage) pipeline.findStage(engine.taskType)
        OpenShift platform = (OpenShift) stage.platform
        Deployment deployment = stage.deployment
//        List<Template> templates = stage.templates
        Template template = stage.template
        def steps = pipeline.steps

        String appPakage = PipelineUtils.normalizePath(app.id + "/" + engine.appPackage)
        appPakage = PipelineUtils.resolveVars([app: app], appPakage)

        steps.println("Building container image for " + appPakage)

        steps.sh "rm -rf oc-build && mkdir -p oc-build/deployments"
        steps.sh "cp ${appPakage} oc-build/deployments/"

        steps.openshift.withProject(platform.project) {
            def processed = applyTemplate(pipeline, template, ["platform": platform, "deployment": deployment, "engine": engine, "app": app, "stage": stage], steps)
            steps.println("Processed: ${processed}")
            def bc = processed.narrow('bc')
            steps.println("BC: ${bc}")
            steps.println('Starting a container build from the created BuildConfig...')
            def buildSelector = bc.startBuild("--from-dir=oc-build/deployments", "--wait=true")
        }
    }

    static void applyTemplates(Pipeline pipeline, List<Template> templates, Map bindings, steps) {
        for (Template template : templates) {
            applyTemplate(pipeline, template, bindings, steps)
        }
    }

    static applyTemplate(Pipeline pipeline, Template template, Map bindings, steps) {

        steps.println("Creating template: ${template.name} with policy: ${template.creationPolicy}")
        //Get config repo
        ScmRepository configRepo = PipelineUtils.findConfigRepo(pipeline, template.filePath)
        bindings.put("configRepo", configRepo)
        template.filePath = PipelineUtils.normalizePath(configRepo.localDir + "/" + template.filePath)

        resolveTemplateParams(template, bindings)
        steps.println("Template params: ${template.parsedParams}")

        def templateFile = steps.readYaml file: template.filePath
        def processed = steps.openshift.process(templateFile, template.parsedParams)
        steps.println("processed: ${processed}")

        processed.withEach {
            createResource(it.object(), template.creationPolicy, steps)
        }

        return processed
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

    static void resolveTemplatesParams(List<Template> templates, Map bindings) {
        for (Template template : templates) {
            resolveTemplateParams(template, bindings)
        }
    }

    static void resolveTemplateParams(Template template, Map bindings) {
        String paramValue
        for (String param : template.params.keySet()) {
            paramValue = PipelineUtils.resolveVars(bindings, template.params.get(param))
            template.parsedParams += "-p ${param}=\'${paramValue}\' "
        }
    }
}
