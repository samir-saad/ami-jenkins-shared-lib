import hudson.model. *
import org.ssaad.ami.pipeline.common.FactoryProvider
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineFactory
import org.ssaad.ami.pipeline.common.PipelineTest


def call(Closure body) {

	Pipeline superPipeline = ((PipelineFactory) FactoryProvider.getFactory("Pipeline")).create("Maven")

	pipeline {

		//agent { node { label 'maven' } }
		agent any

		stages {
			stage("Init Pipeline") {
				steps {
					script {
						//println("Pipeline Configs: \n" + new JsonBuilder(superPipeline).toPrettyString())
						new PipelineTest().printPipeline(this)
					}
				}
			}
		}
	}
}
