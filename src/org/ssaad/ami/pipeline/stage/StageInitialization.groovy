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

    StageInitialization(TaskType taskType, EngineType engineType, PluginType pluginType,
                        AppRuntimeType appRuntimeType, EnvironmentType environmentType,
                        DeploymentType deploymentType, TemplateType templateType, PlatformType platformType) {
        this.taskType = taskType
        this.engineType = engineType
        this.pluginType = pluginType
        this.appRuntimeType = appRuntimeType
        this.environmentType = environmentType
        this.deploymentType = deploymentType
        this.templateType = templateType
        this.platformType = platformType
    }
//    @NonCPS
    /*static StageInitialization create(TaskType taskType, EngineType engineType, List<Object> initialTypes) {
        StageInitialization init = new StageInitialization(taskType, engineType)

        for (Object initialType : initialTypes) {
            if (initialType instanceof PluginType) {
                init.pluginType = initialType
            } else if (initialType instanceof AppRuntimeType) {
                init.appRuntimeType = initialType
            } else if (initialType instanceof EnvironmentType) {
                init.environmentType = initialType
            } else if (initialType instanceof DeploymentType) {
                init.deploymentType = initialType
            } else if (initialType instanceof TemplateType) {
                init.templateType = initialType
            } else if (initialType instanceof PlatformType) {
                init.platformType = initialType
            }
        }
        return init
    }*/

    /*@NonCPS
    static StageInitialization create(TaskType taskType, EngineType engineType, Object... initialTypes) {
        StageInitialization initialization = new StageInitialization(taskType, engineType)

        initialTypes.each {
            initialType ->
                if (initialType instanceof PluginType) {
                    initialization.pluginType = initialType
                } else if (initialType instanceof AppRuntimeType) {
                    initialization.appRuntimeType = initialType
                } else if (initialType instanceof EnvironmentType) {
                    initialization.environmentType = initialType
                } else if (initialType instanceof DeploymentType) {
                    initialization.deploymentType = initialType
                } else if (initialType instanceof TemplateType) {
                    initialization.templateType = initialType
                } else if (initialType instanceof PlatformType) {
                    initialization.platformType = initialType
                }
        }
        return initialization
    }*/
}
