#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println("Get app maven started")

	config.app.id = readMavenPom().getArtifactId()
	config.app.group = readMavenPom().getGroupId()
	config.app.name = readMavenPom().getName()
	config.app.version = readMavenPom().getVersion()
}