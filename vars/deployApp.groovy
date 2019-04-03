#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config, String env) {

	println("Deploy App started ")

	//println("Check cloud Provider ")
	//if(config.cdConfig.containerBuildStage.cloudProvider == "ocp" ) {
		println("Deploy to OCP " + env)
		deployToOCP(config, env)
	//}
}