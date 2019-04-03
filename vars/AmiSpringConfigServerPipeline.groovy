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

						// Dev template
						config.cdConfig.ocpDevDeployment.templates.clear()
						config.cdConfig.ocpDevDeployment.templates.add(TemplateUtils.getSpringConfigServerDC())

						// Test template
						config.cdConfig.ocpTestDeployment.templates.clear()
						config.cdConfig.ocpTestDeployment.templates.add(TemplateUtils.getSpringConfigServerDC())

						// Prod template
						config.cdConfig.ocpProdDeployment.templates.clear()
						config.cdConfig.ocpProdDeployment.templates.add(TemplateUtils.getSpringConfigServerDC())

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

			stage("Build image") {
				when {
					expression {
						return config.cdConfig.containerBuildStage.enable &&
								(config.app.branch.equals("develop") || config.app.branch.startsWith("release-")) &&
								config.app.type.equals("service")
					}
				}
				steps {
					script {
						buildImage(config)
					}
				}
			}

			stage("Deploy to Dev") {
				when {
					expression{
						return config.cdConfig.ocpDevDeployment.enable &&
								(config.app.branch.equals("develop") || config.app.branch.startsWith("release-")) &&
								config.app.type.equals("service")
					}
				}
				steps {
					script {
						deployApp(config, "dev")
					}
				}
			}
			stage("Promote to test") {
				when {
					expression{
						return config.cdConfig.ocpTestDeployment.enable &&
								(config.app.branch.equals("develop") || config.app.branch.startsWith("release-")) &&
								config.app.type.equals("service")
					}
				}
				steps {
					timeout(time:30, unit:'MINUTES') {
						input message: "Promote to Test?", ok: "Promote"
					}
					script {
						promoteToTest(config)
					}
				}
			}
			stage("Deploy to Test") {
				when {
					expression{
						return config.cdConfig.ocpTestDeployment.enable &&
								(config.app.branch.equals("develop") || config.app.branch.startsWith("release-")) &&
								config.app.type.equals("service")
					}
				}
				steps {
					script {
						config.cdConfig.ocpTestDeployment.imageTag = config.app.version
						deployApp(config, "test")
					}
				}
			}

			stage("Promote to Prod") {
				when {
					expression{
						return config.cdConfig.ocpProdDeployment.enable &&
								config.app.branch.startsWith("release-") &&
								config.app.type.equals("service")
					}
				}
				steps {
					timeout(time:30, unit:'MINUTES') {
						input message: "Promote to Prod?", ok: "Promote"
					}
					script {
                        promoteToProd(config)
					}
				}
			}

			stage("Deploy to Prod") {
				when {
					expression{
						return config.cdConfig.ocpProdDeployment.enable &&
								config.app.branch.startsWith("release-") &&
								config.app.type.equals("service")
					}
				}
				steps {
					script {
						config.cdConfig.ocpProdDeployment.imageTag = config.app.version
						deployApp(config, "prod")
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
