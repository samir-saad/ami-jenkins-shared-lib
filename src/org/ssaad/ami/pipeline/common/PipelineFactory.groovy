package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.common.types.*
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineFactory {

    Pipeline create(PipelineInitialization init) {

        Pipeline pipeline = new Pipeline()
        switch (init.pipelineType) {
            case PipelineType.SPRING_MAVEN_OPENSHIFT:
                pipeline.id = "maven-spring-ocp-pipeline"
                pipeline.name = "maven-spring-ocp-pipeline"

                init.addStageInit(StageInitialization.create(TaskType.CODE_BUILD, EngineType.MAVEN))
                init.addStageInit(StageInitialization.create(TaskType.UNIT_TESTS, EngineType.MAVEN))
                init.addStageInit(StageInitialization.create(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN))

                init.addStageInit(StageInitialization.create(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT, PluginType.OPENSHIFT_S2I,
                        PlatformType.OPENSHIFT, EnvironmentType.DEV, AppRuntimeType.JDK, DeploymentType.RECREATE, TemplateType.S2I_BUILD))

                break
        }

        pipeline.init(init)

        return pipeline
    }
}
