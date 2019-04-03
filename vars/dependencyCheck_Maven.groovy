#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println(config.ciConfig.codeSecurityStage.name + " " + config.ciConfig.codeSecurityStage.engine + " started")

	dir(config.app.id) {
		sh "mvn ${config.ciConfig.codeSecurityStage.goals} ${config.ciConfig.codeSecurityStage.params}"
	}
}