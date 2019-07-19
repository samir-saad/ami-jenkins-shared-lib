import hudson.model. *

import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineEnum
import org.ssaad.ami.pipeline.common.PipelineFactory
import org.ssaad.ami.pipeline.common.PipelineTest


def call(Closure body) {

	Pipeline myPipeline = new PipelineFactory().create(PipelineEnum.MAVEN_SPRING_OPENSHIFT)

	pipeline {

		agent { node { label 'maven' } }
		//agent any

		stages {
			stage("Init Pipeline") {
				steps {
					script {
						//println("Pipeline Configs: \n" + new JsonBuilder(superPipeline).toPrettyString())
						new PipelineTest().printPipeline(this)
					}
				}
			}
			stage("Execute Pipeline") {
				steps {
					script {
						//myPipeline.stages.get("build").execute(this)
						executeStages(this, myPipeline)
					}
				}
			}
		}
	}
}
