#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Build Image started ")

	println("Check cloud Provider ")
	if(config.cdConfig.containerBuildStage.cloudProvider == "ocp") {
		println("Build on" + " " + "${config.cdConfig.containerBuildStage.cloudProvider}")
		buildImage_OCP(config)
	}
}