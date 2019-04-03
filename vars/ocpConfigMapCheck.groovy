#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment, String cmFilePath) {

	println("OCP Config Map Check: " + cmFilePath)

    String configMapName = getFileNameFromPath(cmFilePath)
    println("configMapName: " + configMapName)

    openshift.withCluster() {
        openshift.withProject(ocpDeployment.project) {

            // Get config map from OCP project by nam
            println("Check Config Map exists: " + cmFilePath)

            def cmSelector = openshift.selector("cm", configMapName)
            if(!cmSelector.exists()){
                // If config map doesn't exist in OCP project - create it
                println("Config Map doesn't exist: " + cmFilePath)
                ocpCreateConfigMap(config, ocpDeployment, cmFilePath)

            } else {
                // If config map exists
                println("Config Map exist: " + cmFilePath)
                def cmObject = cmSelector.object()
                // Get commit label
                println("get cm commit label")
                if(cmObject.metadata.labels == null){
                    println("delete cm")
                   // sh "oc delete cm ${configMapName} -n ${projectName} || true"
                    ocpCreateConfigMap(config, ocpDeployment, cmFilePath)
                } else {
                    String cmCommit = cmObject.metadata.labels[ "commit" ]
                    println("cm commit label: " + cmCommit)
                    // If commit label equals latest commit, do nothing
                    // If commits are not the same
                    if(!config.externalConfig.latestCommit.equals(cmCommit)){
                        // If commit label doesn't equal latest commit
                        // Get the diff between commits
                        // If the config map file changed between commits, recreate it.
                        if(checkFileChanges(config.externalConfig.localDir, cmCommit, config.externalConfig.latestCommit, cmFilePath)){
                            println("delete cm")
                            ocpCreateConfigMap(config, ocpDeployment, cmFilePath)
                        }
                    }
                }
            }
        }
    }
}