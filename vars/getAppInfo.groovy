#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Get app info started")

	println("Check engine")
	if(config.ciConfig.codeBuildStage.engine == "maven") {
		println("Engine is maven")
		getAppInfo_Maven(config)
	}
}