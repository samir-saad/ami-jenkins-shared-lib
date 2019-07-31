package org.ssaad.ami.pipeline.common


import org.ssaad.ami.pipeline.common.types.*
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineFactory {

    //@NonCPS
    Pipeline create(PipelineInitialization init) {

        Pipeline pipeline = new Pipeline()
        switch (init.pipelineType) {
            case PipelineType.SPRING_MAVEN_OPENSHIFT:
                pipeline.id = "maven-spring-ocp-pipeline"
                pipeline.name = "maven-spring-ocp-pipeline"

                init.stageInitMap.put(TaskType.CODE_BUILD, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
                init.stageInitMap.put(TaskType.UNIT_TESTS, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
                init.stageInitMap.put(TaskType.BINARIES_ARCHIVE, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))

                init.stageInitMap.put(TaskType.CONTAINER_BUILD, new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT,
                        PluginType.OPENSHIFT_S2I, AppRuntimeType.JDK, EnvironmentType.DEV,
                        DeploymentType.BASIC, TemplateType.S2I_BUILD, PlatformType.OPENSHIFT))

                break
        }

        pipeline.init(init)

        init.steps.println("Initial pipeline:")
        pipeline.print()

        return pipeline
    }
}
