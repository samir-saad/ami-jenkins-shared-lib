#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment) {

	println("Automated tests started: $ocpDeployment.testing.name")

	println("Check engine")
	if(ocpDeployment.testing.engine == "maven") {
		println("Engine is maven/SoapUI")
		executeAutomatedTests_SoapUI(config, ocpDeployment)
	}
}