package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.*
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineFactory {

    @NonCPS
    Pipeline create(PipelineInitialization init) {

        Pipeline pipeline = new Pipeline()
        switch (init.pipelineType) {
            case PipelineType.SPRING_MAVEN_OPENSHIFT:
                pipeline.id = "maven-spring-ocp-pipeline"
                pipeline.name = "maven-spring-ocp-pipeline"

//                TaskType taskType, EngineType engineType, PluginType pluginType,
//                    AppRuntimeType appRuntimeType, EnvironmentType environmentType,
//                    DeploymentType deploymentType, TemplateType templateType, PlatformType platformType)
//
                break
        }

        pipeline.init(init)

        return pipeline
    }
}
