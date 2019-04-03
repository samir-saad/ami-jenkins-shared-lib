#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Quality Scan started ")

	println("Check engine")
	if(config.ciConfig.codeQualityStage.engine == "sonar") {
		println("Engine is maven")
		qualityScan_Sonar(config)
	}
}