import hudson.model.*

import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineFactory
import org.ssaad.ami.pipeline.common.PipelineInitialization
import org.ssaad.ami.pipeline.common.types.AppRuntimeType
import org.ssaad.ami.pipeline.common.types.DeploymentType
import org.ssaad.ami.pipeline.common.types.EngineType
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.PipelineType
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
                        myPipeline.id = "maven-spring-ocp-pipeline"
                        myPipeline.name = "maven-spring-ocp-pipeline"

                        PipelineInitialization init = new PipelineInitialization()
                        init.pipelineType = PipelineType.SPRING_MAVEN_OPENSHIFT
                        init.buildId = "${BUILD_TAG}"
                        init.scm = ScmType.GIT
                        init.steps = this
                        init.env = env

                        init.stageInitMap.put(TaskType.CODE_BUILD, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
                        init.stageInitMap.put(TaskType.UNIT_TESTS, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
                        init.stageInitMap.put(TaskType.BINARIES_ARCHIVE, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))

                        init.stageInitMap.put(TaskType.CONTAINER_BUILD, new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT,
                                PluginType.OPENSHIFT_S2I, AppRuntimeType.JDK, EnvironmentType.DEV,
                                DeploymentType.RECREATE, TemplateType.S2I_BUILD, PlatformType.OPENSHIFT))
                        
//                        init.addStageInit(new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN, null, null, null, null, null, null))
//                        init.addStageInit(new StageInitialization(TaskType.UNIT_TESTS, EngineType.MAVEN, null, null, null, null, null, null))
//                        init.addStageInit(new StageInitialization(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN, null, null, null, null, null, null))
//
//                        init.addStageInit(new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT, PluginType.OPENSHIFT_S2I,
//                                AppRuntimeType.JDK, EnvironmentType.DEV,
//                                DeploymentType.RECREATE, TemplateType.S2I_BUILD, PlatformType.OPENSHIFT))

                        myPipeline.init(init)
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
