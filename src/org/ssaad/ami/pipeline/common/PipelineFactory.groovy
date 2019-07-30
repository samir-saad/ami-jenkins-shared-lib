package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.common.types.*
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineFactory {

    Pipeline create(PipelineInitialization init) {
        init.steps.println("Pipeline Factory")

        Pipeline pipeline = new Pipeline()
        switch (init.pipelineType) {
            case PipelineType.SPRING_MAVEN_OPENSHIFT:
                init.steps.println("SPRING_MAVEN_OPENSHIFT")
                pipeline.id = "maven-spring-ocp-pipeline"
                pipeline.name = "maven-spring-ocp-pipeline"

                init.steps.println("Create stage init CODE_BUILD")
                StageInitialization stageInit = StageInitialization.create(TaskType.CODE_BUILD, EngineType.MAVEN, null)
                init.steps.println("Created stage init " + stageInit.toString())
                init.steps.println("Add stage init ")
                init.addStageInit(stageInit)
                init.steps.println("Added stage init ")

                init.steps.println("Create stage init UNIT_TESTS")
                stageInit = StageInitialization.create(TaskType.UNIT_TESTS, EngineType.MAVEN, null)
                init.steps.println("Created stage init " + stageInit.toString())
                init.steps.println("Add stage init ")
                init.addStageInit(stageInit)
                init.steps.println("Added stage init ")

                init.steps.println("Create stage init BINARIES_ARCHIVE")
                stageInit = StageInitialization.create(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN, null)
                init.steps.println("Created stage init " + stageInit.toString())
                init.steps.println("Add stage init ")
                init.addStageInit(stageInit)
                init.steps.println("Added stage init ")

                init.steps.println("Create stage init CONTAINER_BUILD")
                stageInit = StageInitialization.create(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT, [PluginType.OPENSHIFT_S2I,
                                                                                                        PlatformType.OPENSHIFT, EnvironmentType.DEV, AppRuntimeType.JDK, DeploymentType.RECREATE, TemplateType.S2I_BUILD])
                init.steps.println("Created stage init " + stageInit.toString())
                init.steps.println("Add stage init ")
                init.addStageInit(stageInit)
                init.steps.println("Added stage init ")

                break
        }

       // pipeline.init(init)

        return pipeline
    }
}
