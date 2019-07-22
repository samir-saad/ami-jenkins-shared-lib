import hudson.model. *
import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineInitialization
import org.ssaad.ami.pipeline.common.ScmType
import org.ssaad.ami.pipeline.common.TaskType
import org.ssaad.ami.pipeline.engine.EngineInitialization

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
						initialization.buildId = "${JOB_NAME}-${BRANCH_NAME}-${BUILD_NUMBER}"
						initialization.scm = ScmType.GIT
						initialization.steps = this

						initialization.stageInitMap.put(TaskType.CODE_BUILD, new EngineInitialization(EngineType.MAVEN, null))

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
