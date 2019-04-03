#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Template

def call(PipelineConfig config, String env) {

    OcpDeployment ocpDeployment
    String dcName

    if("dev".equals(env)){
        ocpDeployment = config.cdConfig.ocpDevDeployment
    } else if("test".equals(env)){
        ocpDeployment = config.cdConfig.ocpTestDeployment
    } else if("prod".equals(env)){
        ocpDeployment = config.cdConfig.ocpProdDeployment
    }

    ocpDeployment.buildVersion = config.app.version.toLowerCase().replace(".", "-")

    println("OCP project name: " + ocpDeployment.project)

    stage("Check Config") {
        println("Check Config " + env)
        //ocpConfigCheck(config, env, ocpDeployment)
        //ocpResolveParams(config, ocpDeployment)
        if("recreate".equals(ocpDeployment.type)){
            ocpConfigRecreate(config, ocpDeployment)
        } else  if("blue-green".equals(ocpDeployment.type)){
            ocpConfigBlueGreen(config, ocpDeployment)
        }
    }

    stage("Deploy") {
        println("Deploy to " + env)
        openshift.withCluster() {
            openshift.withProject(ocpDeployment.project) {
                Template dcTemplate

                for (Template t : ocpDeployment.templates){
                    if("dc".equals(t.type)){
                        dcTemplate = t
                        dcName = t.params.get("OCP_OBJECT_NAME")
                    }
                }
                println(dcName)
                def dcSelector = openshift.selector("dc", dcName)
                println(dcSelector)

                // Mount secrets
                Map<String, String> secretsMap = ocpGetAppSecrets(config, ocpDeployment)
                println(secretsMap)
                for(String key : secretsMap.keySet()){
                    ocpDeployment.secretsMap.put(key, new String(secretsMap.get(key).decodeBase64()))
                }
                ocpMountEnvVars(ocpDeployment, dcName)

                // Disable auto scale
                sh "oc delete hpa ${dcName} -n ${ocpDeployment.project} || true"

                dcSelector.rollout().latest()
                def deployment = "${dcName}-${dcSelector.object().status.latestVersion}"
                def pods = openshift.selector('pods', [deployment: "${deployment}"])

                timeout(5) {
                    pods.untilEach(Integer.parseInt(dcTemplate.params[ "REPLICAS" ])) {
                        return it.object().status.containerStatuses.every {
                            it.ready
                        }
                    }
                }
            }
        }
    }

    // Do auto scale
    if(ocpDeployment.autoScaling.enable){
        stage(ocpDeployment.autoScaling.name) {

            //Wait for CPU to idle
            sleep(time:30,unit:"SECONDS")

            sh "oc autoscale dc/${dcName} " +
                    "--min ${ocpDeployment.autoScaling.minReplicas} " +
                    "--max ${ocpDeployment.autoScaling.maxReplicas} " +
                    "--cpu-percent=${ocpDeployment.autoScaling.cpuThresholdPercentage} " +
                    "-n ${ocpDeployment.project} || true"
        }
    }

    // Rollback or switch
    if ("blue-green".equals(ocpDeployment.type)){

        stage("Switch Traffic or Rollback ") {
            timeout(time: 30, unit: 'MINUTES') {
                def choices = input(
                        message: "Switch traffic?",
                        ok: "OK",
                        parameters: [choice(name: 'APPROVE', choices: 'confirm\nrollback', description: 'Switch Traffic?')]
                )
                if ("confirm".equals(choices)) {
                    script {
                        ocpSwitchRoute(config, ocpDeployment, ocpDeployment.tag)
                    }
                } else {
                    sh "oc delete dc,svc,route,is,builds,rc,hpa " +
                            "-l app=${config.app.id},deployment-tag=${ocpDeployment.tag} " +
                            "-n ${ocpDeployment.project} || true"

                    currentBuild.result = 'ABORTED'
                    error("Release aborted by user")
                }
            }
        }

    }

    // Run automated tests
    if(ocpDeployment.testing != null && ocpDeployment.testing.enable){
        stage(ocpDeployment.testing.name) {
            boolean executeTests = true
            if(ocpDeployment.testing.requiresConfirmation){
                timeout(time: 30, unit: 'MINUTES') {
                    def choices = input(
                            message: "Execute ${ocpDeployment.testing.name}",
                            ok: "OK",
                            parameters: [choice(name: 'APPROVE', choices: 'yes\nno', description: 'Execute tests?')]
                    )
                    if ("no".equals(choices)) {
                        executeTests = false
                    }
                }
            }

            if (executeTests){
                Map<String, String> testSecretsMap = ocpGetAppTestSecrets(config, ocpDeployment)
                println(testSecretsMap)
                for(String key : testSecretsMap.keySet()){
                    ocpDeployment.testSecretsMap.put(key, new String(testSecretsMap.get(key).decodeBase64()))
                }
                executeAutomatedTests(config, ocpDeployment)
            }
        }
    }

    // Routes IPs Whitelisting
    if (ocpDeployment.routeIPsWhitelist != null && !ocpDeployment.routeIPsWhitelist.trim().equals("")){
        println("IPs Whitelisting")
        sh "oc annotate route ${config.app.id} haproxy.router.openshift.io/ip_whitelist=\"${ocpDeployment.routeIPsWhitelist}\" --overwrite -n ${ocpDeployment.project}"
    }

    // Confirm release
    if ("blue-green".equals(ocpDeployment.type)) {
        stage("Confirm Release") {
            timeout(time: 30, unit: 'MINUTES') {
//                    input message: "Confirm release?", ok: "Confirm"
                def choices = input(
                        message: "Confirm release?",
                        ok: "OK",
                        parameters: [choice(name: 'APPROVE', choices: 'confirm\nrollback', description: 'Confirm release?')]
                )
                if ("confirm".equals(choices)) {
                    println("rollout")
                    // Delete old deployment completely
                    sh "oc delete dc,svc,route,is,builds,rc,hpa " +
                            "-l app=${config.app.id},deployment-tag=${ocpDeployment.oldTag} " +
                            "-n ${ocpDeployment.project} || true"

                    // Delete new deployment temp routes
                    sh "oc delete route " +
                            "-l app=${config.app.id},deployment-tag=${ocpDeployment.tag} " +
                            "-n ${ocpDeployment.project} || true"

                } else {
                    println("rollback")
                    // Delete new deployment completely
                    sh "oc delete dc,svc,route,is,builds,rc,hpa " +
                            "-l app=${config.app.id},deployment-tag=${ocpDeployment.tag} " +
                            "-n ${ocpDeployment.project} || true"

                    // Switch prod routes to old service
                    ocpSwitchRoute(config, ocpDeployment, ocpDeployment.oldTag)

                    currentBuild.result = 'ABORTED'
                    error("Release aborted by user")
                }
            }
        }
    }
}
