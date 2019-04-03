#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment, String templateFilePath) {

//	println("Search template change")
//    String templateName = getFileNameFromPath(templateFilePath)

    openshift.withCluster() {
        openshift.withProject(ocpDeployment.project) {

            // Get deployment config from OCP project by nam
            println("Check template exists: " + templateFilePath)

            def dcSelector = openshift.selector("dc", config.app.id)
            if(!dcSelector.exists()){
                // If deployment config doesn't exist in OCP project - create it
                println("deployment config doesn't exist: " + templateFilePath)
                ocpCreateTemplate(config, ocpDeployment, templateFilePath)

            } else {
                // If deployment config exists
                println("deployment config exist: " + templateFilePath)
                def dcObject = dcSelector.object()
                // Get commit label
                println("get dc commit label")
                if(dcObject.metadata.labels == null){
                    println("delete dc")
                    ocpCreateTemplate(config, ocpDeployment, templateFilePath)
                } else {
                    String dcCommit = dcObject.metadata.labels[ "commit" ]
                    println("dc commit label: " + dcCommit)
                    // If commit label equals latest commit, do nothing
                    // If commits are not the same
                    if(!config.externalConfig.latestCommit.equals(dcCommit)){
                        // If commit label doesn't equal latest commit
                        // Get the diff between commits
                        // If the deployment config file changed between commits, recreate it.
                        if(checkFileChanges(config.externalConfig.localDir, dcCommit, config.externalConfig.latestCommit, templateFilePath)){
                            println("delete dc")
                            ocpCreateTemplate(config, ocpDeployment, templateFilePath)
                        }
                    }
                }
            }
        }
    }
}