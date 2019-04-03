#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Code build started")

	println("Check engine")
	if(config.ciConfig.codeBuildStage.engine == "maven") {
		println("Engine is maven")
		codeBuild_Maven(config)
	}
}