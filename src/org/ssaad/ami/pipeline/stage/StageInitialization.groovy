package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.types.*

class StageInitialization implements Serializable {

    TaskType taskType
    EngineType engineType
    PluginType pluginType
    AppRuntimeType appRuntimeType
    EnvironmentType environmentType
    DeploymentType deploymentType
    TemplateType templateType
    PlatformType platformType

    StageInitialization(TaskType taskType, EngineType engineType) {
        this.taskType = taskType
        this.engineType = engineType
    }

    StageInitialization(TaskType taskType, EngineType engineType, Object... initialTypes) {
        this(taskType, engineType)

        initialTypes.each {
            initialType ->
                if (initialType instanceof PluginType) {
                    this.pluginType = initialType
                } else if (initialType instanceof AppRuntimeType) {
                    this.appRuntimeType = initialType
                } else if (initialType instanceof EnvironmentType) {
                    this.environmentType = initialType
                } else if (initialType instanceof DeploymentType) {
                    this.deploymentType = initialType
                } else if (initialType instanceof TemplateType) {
                    this.templateType = initialType
                } else if (initialType instanceof PlatformType) {
                    this.platformType = initialType
                }
        }
    }
}
