#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Init Maven")

	sh "cp -f $WORKSPACE/${config.ciConfig.codeBuildStage.configDir}/* /home/jenkins/.m2/"
}