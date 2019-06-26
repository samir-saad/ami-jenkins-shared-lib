import groovy.json.JsonBuilder
import hudson.model. *
import org.ssaad.ami.pipeline.test.Test

//import org.ssaad.ami.pipeline.FactoryProvider
//import org.ssaad.ami.pipeline.Pipeline
//import org.ssaad.ami.pipeline.PipelineFactory

def call(Closure body) {

	//Pipeline superPipeline = ((PipelineFactory) FactoryProvider.getFactory("Pipeline")).create("Maven")

	pipeline {

		//agent { node { label 'maven' } }
		agent any

		stages {
			stage("Init Pipeline") {
				steps {
					script {
						//println("Pipeline Configs: \n" + new JsonBuilder(superPipeline).toPrettyString())
						new Test().test()
					}
				}
			}
		}
	}
}
