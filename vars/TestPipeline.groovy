import hudson.model. *

import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineEnum
import org.ssaad.ami.pipeline.common.PipelineFactory
import org.ssaad.ami.pipeline.common.PipelineInitialization
import org.ssaad.ami.pipeline.common.PipelineTest


def call(Closure body) {

	Pipeline myPipeline

	pipeline {

		//agent { node { label 'maven' } }
		agent any

		stages {
			stage("Init Pipeline") {
				steps {
					script {
						//println("Pipeline Configs: \n" + new JsonBuilder(superPipeline).toPrettyString())
						myPipeline = new Pipeline()
						PipelineInitialization initialization = new PipelineInitialization()
						initialization.id = "maven-spring-ocp-pipeline"
						initialization.name = "maven-spring-ocp-pipeline"
						myPipeline.init(initialization)

						myPipeline.print(this)
					}
				}
			}
			/*stage("Execute Pipeline") {
				steps {
					script {
						//myPipeline.stages.get("build").execute(this)
						executeStages(this, myPipeline)
					}
				}
			}*/
		}
	}
}
