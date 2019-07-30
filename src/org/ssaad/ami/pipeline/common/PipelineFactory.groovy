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

//                TaskType taskType, EngineType engineType, PluginType pluginType,
//                    AppRuntimeType appRuntimeType, EnvironmentType environmentType,
//                    DeploymentType deploymentType, TemplateType templateType, PlatformType platformType)
//
                init.steps.println("Add stages")
                break
        }

        pipeline.init(init)

        return pipeline
    }
}
