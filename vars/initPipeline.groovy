#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.utils.PipelineUtils
import groovy.json.JsonSlurper

def call(/*String config*/) {

	println("Init Pipeline Script")

	if ("master".equals(BRANCH_NAME)){
		currentBuild.result = 'ABORTED'
		error("master branches aren't allowed")
	}

	//slackNotification.info message: 'STARTED', appendBuildInfo: true
	PipelineConfig pipelineConfig

	// Get Json
	if (fileExists('pipeline-config.json')) {
		String config = readFile file: 'pipeline-config.json'
		println("Params: \n" + config)
		// using Map to convert to Person object type
		pipelineConfig = new PipelineConfig(new JsonSlurper().parseText(config))  //PipelineUtils.initConfig(config)
	} else if (fileExists('pipeline-config.yaml')) {
		// Do yaml init
	} else {
		println("No init params")
		pipelineConfig = new PipelineConfig()
	}


	// Workspace base directory
	pipelineConfig.baseDir = WORKSPACE

	println("Get app info")
	// App info
	//pipelineConfig.app =
	getAppInfo(pipelineConfig)

	// Branch
	pipelineConfig.app.branch = BRANCH_NAME

	// Fix App dir
	initPipeline_FixAppDirectory(pipelineConfig)

	//println("Pipeline Configs: \n" + new JsonBuilder(pipelineConfig).toPrettyString())

	println("return config")
	return pipelineConfig
}