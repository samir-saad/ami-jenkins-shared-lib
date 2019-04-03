#!/usr/bin/groovy 
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, String env, OcpDeployment ocpDeployment) {

	println("OCP config check")

	dir(config.externalConfig.localDir) {
		config.externalConfig.latestCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()

        print("Latest config commit " + config.externalConfig.latestCommit)
	}

    // List of config maps
    def cmFiles = findFiles(glob: "${config.externalConfig.localDir}/deploy/ocp/configmaps/${env}/*.yaml")
    println("cmFiles: ${cmFiles}")

    for (def cmFile : cmFiles) {
        print("Chech CM ${cmFile}")
        ocpConfigMapCheck(config, ocpDeployment, "${cmFile}")
    }

    //List of Templates
    def templateFilePath = config.externalConfig.localDir + "/deploy/ocp/templates/" + ocpDeployment.template
    print("Chech Template ${templateFilePath}")
    ocpTemplateCheck(config, ocpDeployment, templateFilePath)
}