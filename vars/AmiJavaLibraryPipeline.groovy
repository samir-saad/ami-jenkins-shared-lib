import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.utils.TemplateUtils
import groovy.json.JsonBuilder
import hudson.model. *

def call(Closure body) {
//	def customConfig = [:]
//    body.resolveStrategy = Closure.DELEGATE_FIRST
//    body.delegate = customConfig
//    body()

	PipelineConfig config

	pipeline {

		agent { node { label 'maven' } }
		//agent any

		stages {
			stage("Init Pipeline") {
				steps {
					script {
						//config = initPipeline(customConfig.config)
						config = initPipeline()
						println("Pipeline Configs: \n" + new JsonBuilder(config).toPrettyString())
					}
				}
			}

			stage("Init Config") {
				steps {
					script {
						initConfig(config)
					}
				}
			}

			stage("Build") {
				when {
					expression { return config.ciConfig.codeBuildStage.enable }
				}
				steps {
					script {
						codeBuild(config)
					}
				}
			}

			stage("Unit & Integration Tests") {
				when {
					expression { return config.ciConfig.codeTestStage.enable }
				}
				steps {
					script {
						codeTest(config)
					}
				}
			}

			stage("Quality Scan") {
				when {
					expression { return config.ciConfig.codeQualityStage.enable }
				}
				steps {
					script {
						qualityScan(config)
					}
				}
			}

			stage("Dependencies Check") {
				when {
					expression { return config.ciConfig.codeSecurityStage.enable }
				}
				steps {
					script {
						dependencyCheck(config)
					}
				}
			}

			stage("Archive") {
				when {
					expression {
						return config.ciConfig.codeArchiveStage.enable &&
								(config.app.branch.equals("develop") || config.app.branch.startsWith("release-"))
					}
				}
				steps {
					script {
						archiveArtifacts(config)
					}
				}
			}
		}

		post('Publish Results') {
			always {
				slackBuildResult()
			}
		}
	}
}
