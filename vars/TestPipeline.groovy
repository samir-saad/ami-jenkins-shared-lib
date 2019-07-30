import hudson.model. *
import org.ssaad.ami.pipeline.common.types.AppRuntimeType
import org.ssaad.ami.pipeline.common.types.DeploymentType
import org.ssaad.ami.pipeline.common.types.EngineType
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineInitialization
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.PlatformType
import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.common.types.ScmType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.common.types.TemplateType
import org.ssaad.ami.pipeline.stage.StageInitialization

def call(Closure body) {

	Pipeline myPipeline

	pipeline {

		agent { node { label 'maven' } }
		//agent any

		stages {
			stage("Create Pipeline") {
				steps {
					script {
						//println("Pipeline Configs: \n" + new JsonBuilder(superPipeline).toPrettyString())
						echo("Creating pipeline ...")
						myPipeline = new Pipeline()
						PipelineInitialization initialization = new PipelineInitialization()
						initialization.id = "maven-spring-ocp-pipeline"
						initialization.name = "maven-spring-ocp-pipeline"
						initialization.buildId = "${BUILD_TAG}"
						initialization.scm = ScmType.GIT
						initialization.steps = this
						initialization.env = env

						initialization.addStageInit(new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
						initialization.addStageInit(new StageInitialization(TaskType.UNIT_TESTS, EngineType.MAVEN))
						initialization.addStageInit(new StageInitialization(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN,))

						initialization.addStageInit(new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT, PluginType.OPENSHIFT_S2I,
								PlatformType.OPENSHIFT, EnvironmentType.DEV, AppRuntimeType.JDK, DeploymentType.RECREATE, TemplateType.S2I_BUILD))

						myPipeline.init(initialization)
					}
				}
			}
			stage("Execute Pipeline") {
				steps {
					script {
						echo("Executing pipeline ...")
						myPipeline.execute()
					}
				}
			}
		}
	}
}
