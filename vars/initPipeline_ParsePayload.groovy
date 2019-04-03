#!/usr/bin/groovy

import org.ssaad.ami.pipeline.config.PipelineConfig

def call(customConfig) {

	println "Parse Payload"
	
	println customConfig.payload
	
	def params = new groovy.json.JsonSlurper().parseText(customConfig.payload)
	
	def config = new PipelineConfig()
	
	// Workspace base directory
	config.baseDir = pwd()
	
	// External build and deployment configs
	config.external.repoUrl = params?.external?.repoUrl ?: "git@github.com:bcgov-c/xxx-pipeline-configs.git"
	config.external.branch = params?.external?.branch ?: "develop"
	config.external.credentials = params?.external?.credentials ?: "xxx-jenkins"
	config.external.localDir = params?.external?.localDir ?: "external"
	
	// Build engine configs
	config.ci.build.enable = params?.ci?.build?.enable ?: true
	config.ci.build.id = params?.ci?.build?.id ?: "maven"
	config.ci.build.type = params?.ci?.build?.type ?: "maven"
	config.ci.build.credentials = params?.ci?.build?.credentials ?: ""
	config.ci.build.configDir = params?.ci?.build?.configDir ?: config.baseDir + "/build/maven/settings.xml"
	
	config.app.branch = BRANCH_NAME
	
	// Test engine configs
	config.ci.test.enable = params?.ci?.test?.enable ?: true
	config.ci.test.id = params?.ci?.test?.id ?: "junit"
	config.ci.test.type = params?.ci?.test?.type ?: "junit"
	config.ci.test.credentials = params?.ci?.test?.credentials ?: ""
	config.ci.test.configDir = params?.ci?.test?.configDir ?: ""
	
	// Static code quality engine configs
	config.ci.codeQuality.enable = params?.ci?.codeQuality?.enable ?: true
	config.ci.codeQuality.id = params?.ci?.codeQuality?.id ?: "sonar"
	config.ci.codeQuality.type = params?.ci?.codeQuality?.type ?: "sonar"
	config.ci.codeQuality.credentials = params?.ci?.codeQuality?.credentials ?: ""
	config.ci.codeQuality.configDir = params?.ci?.codeQuality?.configDir ?: ""
	
	// Code dependencies check engine configs
	config.ci.codeSecurity.enable = params?.ci?.codeSecurity?.enable ?: true
	config.ci.codeSecurity.id = params?.ci?.codeSecurity?.id ?: "owasp"
	config.ci.codeSecurity.type = params?.ci?.codeSecurity?.type ?: "owasp"
	config.ci.codeSecurity.credentials = params?.ci?.codeSecurity?.credentials ?: ""
	config.ci.codeSecurity.configDir = params?.ci?.codeSecurity?.configDir ?: ""
	
	// Artifacts archive engine configs
	config.ci.archive.enable = params?.ci?.archive?.enable ?: true
	config.ci.archive.id = params?.ci?.archive?.id ?: "nexus"
	config.ci.archive.type = params?.ci?.archive?.type ?: "nexus"
	config.ci.archive.credentials = params?.ci?.archive?.credentials ?: "nexus"
	config.ci.archive.configDir = params?.ci?.archive?.configDir ?: ""
	
	return config
}