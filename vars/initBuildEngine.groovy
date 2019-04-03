#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Init build engine")

	println("Check engine")
	if(config.ciConfig.codeBuildStage.engine == "maven") {
		println("Engine is maven")
		initBuildEngine_Maven(config)
	}
}