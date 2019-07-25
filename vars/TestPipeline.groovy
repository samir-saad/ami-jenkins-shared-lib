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
			stage("Create Pipeline") {
				steps {
					script {
						//println("Pipeline Configs: \n" + new JsonBuilder(superPipeline).toPrettyString())
						myPipeline = new Pipeline()
						PipelineInitialization initialization = new PipelineInitialization()
						initialization.id = "maven-spring-ocp-pipeline"
						initialization.name = "maven-spring-ocp-pipeline"
						initialization.buildId = "${BUILD_TAG}"
						initialization.scm = ScmType.GIT
						initialization.steps = this
						initialization.env = env

						initialization.stageInitMap.put(TaskType.CODE_BUILD, new EngineInitialization(EngineType.MAVEN, null))
						initialization.stageInitMap.put(TaskType.UNIT_TESTS, new EngineInitialization(EngineType.MAVEN, null))
						initialization.stageInitMap.put(TaskType.BINARIES_ARCHIVE, new EngineInitialization(EngineType.MAVEN, null))

						initialization.stageInitMap.put(TaskType.CONTAINER_BUILD, new EngineInitialization(EngineType.OPENSHIFT_S2I, null))

						myPipeline.init(initialization)

						myPipeline.execute()
					}
				}
			}
		}
	}
}
