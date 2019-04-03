#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Template

def call(PipelineConfig config, OcpDeployment ocpDeployment) {

    println("OCP config blue-green")

    //get the number of dc with app tag
    Map deployments = [:]

    openshift.withCluster() {
        openshift.withProject(ocpDeployment.project) {
            def dcSelector = openshift.selector("dc", [app: "${config.app.id}"])

            def dcObject
            def buildLabel
            dcSelector.withEach {
                dcObject = it.object()
                if(dcObject.metadata.labels != null){
                    buildLabel = dcObject.metadata.labels[ "deployment-tag" ]
                }
                deployments.put(it.name(), buildLabel)
            }
        }
    }

    // If 2 or more dc found, this mean another deployment is under the way. stop pipeline
    if (deployments.size() > 1) {
        currentBuild.result = 'ABORTED'
        error("More than one deployment found for app ${config.app.id}. " +
                "Manual cleanup is required.")
    }
    else if(deployments.size() == 1){
        ocpDeployment.oldTag = deployments.get(deployments.keySet().first())
        if("blue".equals(deployments.get(deployments.keySet().first()))){
            ocpDeployment.tag = "green"
        } else if ("green".equals(deployments.get(deployments.keySet().first()))){
            ocpDeployment.tag = "blue"
        } else {
            currentBuild.result = 'ABORTED'
            error("Deployment doesn't have a proper deployment-tag [blue,green].")
        }
    }
    else {
        ocpDeployment.tag = "blue"
    }

    println("Tag: " + ocpDeployment.tag)
    println("Old Tag: " + ocpDeployment.oldTag)

    for (Template t : ocpDeployment.templates) {

        // Fix OCP Object Name in case of blue-green
        if(t.params.get("OCP_OBJECT_NAME") != null){
            t.params.put("OCP_OBJECT_NAME", t.params.get("OCP_OBJECT_NAME") + '-${ocpDeployment.tag}')
        }

        if("route".equals(t.type) && t.params.get("SERVICE_NAME") != null){
            t.params.put("SERVICE_NAME", t.params.get("SERVICE_NAME") + '-${ocpDeployment.tag}')
        }

        ocpResolveParams(config, ocpDeployment, t)

        print("new Param: " + t.parsedParams)

        sh "oc process -f ${config.externalConfig.localDir}/${t.filePath} ${t.parsedParams} " +
                "| oc delete -f- -n ${ocpDeployment.project} || true"

        sh "oc process -f ${config.externalConfig.localDir}/${t.filePath} ${t.parsedParams} " +
                "-l app=${config.app.id}," +
                "app-version=${config.app.version}," +
                "deployment-type=${ocpDeployment.type}," +
                "deployment-tag=${ocpDeployment.tag}," +
                "code-commit=${config.app.latestCommit}," +
                "config-commit=${config.externalConfig.latestCommit} " +
                "| oc create -f- -n ${ocpDeployment.project} || true"
    }
}