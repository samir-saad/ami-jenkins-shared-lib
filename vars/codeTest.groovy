#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Code test started")

	println("Check engine")
	if(config.ciConfig.codeTestStage.engine == "maven") {
		println("Engine is maven")
		codeTest_Maven(config)
	}
}