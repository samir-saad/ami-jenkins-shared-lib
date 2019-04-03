#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Clean config ")

	// Clean ConfigMap
	if( config.cdConfig.ocpDevDeployment.project && cmDevChanged.toBoolean() || config.cdConfig.ocpTestDeployment.project && cmTestChanged.toBoolean()){

		command = "oc delete cm ${config.cdConfig.ocpDevDeployment.configMap} -n " + project + " || true"
		sh command
	}

	// Clean deployment configurations
	if( config.cdConfig.ocpDevDeployment.project && dcDevChanged.toBoolean() || config.cdConfig.ocpTestDeployment.project && dcTestChanged.toBoolean()){

		command = "oc process -f ${config.cdConfig.containerBuildStage.template} -p APP_NAME=${config.app.id} -p BUILD_VERSION=${config.app.version.toLowerCase().replace(".", "-")} -p IMAGE_NAME=${config.app.id} -p IMAGE_TAG=latest | oc delete -f- -n " + project + " || true"
		sh command
	}
}