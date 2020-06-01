package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType
import org.ssaad.ami.pipeline.common.types.EngineType
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.common.types.TaskType

class StageFactory {

    @NonCPS
    Stage create(StageInitialization init, String buildId) {

        Stage stage
        switch (init.taskType) {
            case TaskType.INIT_PIPELINE:
                stage = new InitPipelineStage()
                stage.id = "init-pipeline"
                stage.name = "Init Pipeline"
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
                break
            case TaskType.INIT_CONFIG:
                stage = new InitConfigStage()
                stage.id = "init-config"
                stage.name = "Init Config"
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
                break
            case TaskType.CODE_BUILD:
                stage = new EngineStage()
                stage.id = "code-build"
                stage.name = "Code Build"
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
                break
            case TaskType.UNIT_TESTING:
                stage = new EngineStage()
                stage.id = "unit-testing"
                stage.name = "Unit Testing"
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
                break
            case TaskType.QUALITY_SCANNING:
                stage = new EngineStage()
                stage.id = "quality-scanning"
                stage.name = "Quality Scanning"
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
                break
            case TaskType.DEPENDENCY_CHECK:
                stage = new EngineStage()
                stage.id = "dependency-check"
                stage.name = "Dependency Check"
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
                break
            case TaskType.BINARIES_ARCHIVE:
                stage = new EngineStage()
                stage.id = "binaries-archive"
                stage.name = "Binaries Archive"
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.DEVELOP, BranchType.RELEASE])
                break
            case TaskType.CONTAINER_BUILD:
                stage = new PlatformStage()
                stage.id = "build-container"
                stage.name = "Build Container"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])
                break
            case TaskType.DEPLOY_DEV:
                stage = new PlatformStage()
                stage.id = "deploy-dev"
                stage.name = "Deploy Dev"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])
                stage.testing = (EngineStage)new StageFactory().create(new StageInitialization(TaskType.SYSTEM_TESTING, EngineType.MAVEN, PluginType.MAVEN_SOAPUI, EnvironmentType.DEV), buildId)
                break
            case TaskType.SYSTEM_TESTING:
                stage = new EngineStage()
                stage.id = "system-testing"
                stage.name = "System Testing"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])
                break
            case TaskType.DEPLOY_TEST:
                stage = new PlatformStage()
                stage.id = "deploy-test"
                stage.name = "Deploy Test"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])
                stage.confirmation.enable = true
                stage.testing = (EngineStage)new StageFactory().create(new StageInitialization(TaskType.LOAD_TESTING, EngineType.MAVEN, PluginType.MAVEN_SOAPUI, EnvironmentType.TEST), buildId)
                break
            case TaskType.LOAD_TESTING:
                stage = new EngineStage()
                stage.id = "load-testing"
                stage.name = "Load Testing"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])
                break
            case TaskType.DEPLOY_PROD:
                stage = new PlatformStage()
                stage.id = "deploy-prod"
                stage.name = "Deploy Prod"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.RELEASE], false)
                stage.confirmation.enable = true
                break
        }

        stage.init(init, buildId)

        return stage
    }
}
