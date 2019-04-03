#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	openshift.withCluster() {
		openshift.tag("${config.cdConfig.ocpDevDeployment.project}/${config.app.id}:latest",
				"${config.cdConfig.ocpProdDeployment.project}/${config.app.id}:${config.app.version}")
	}
}