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
                stage.activation = Activation.getInstance([AppType.ANY], [BranchType.DEVELOP, BranchType.DEVELOPMENT, BranchType.RELEASE])
                break
            case TaskType.CONTAINER_BUILD:
                stage = new PlatformStage()
                stage.id = "build-container"
                stage.name = "Build Container"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.DEVELOPMENT, BranchType.RELEASE])
                break
            case TaskType.DEPLOY_DEV:
                stage = new PlatformStage()
                stage.id = "deploy-dev"
                stage.name = "Deploy Dev"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.DEVELOPMENT, BranchType.RELEASE])
                stage.testing = (EngineStage)new StageFactory().create(new StageInitialization(TaskType.SYSTEM_TESTING, EngineType.MAVEN, PluginType.MAVEN_SOAPUI, EnvironmentType.DEV), buildId)
                break
            case TaskType.SYSTEM_TESTING:
                stage = new EngineStage()
                stage.id = "system-testing"
                stage.name = "System Testing"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.DEVELOPMENT, BranchType.RELEASE])
                break
            case TaskType.DEPLOY_QA:
                stage = new PlatformStage()
                stage.id = "deploy-qa"
                stage.name = "Deploy QA"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.DEVELOPMENT, BranchType.RELEASE])
                stage.confirmation.enable = true
                stage.testing = (EngineStage)new StageFactory().create(new StageInitialization(TaskType.LOAD_TESTING, EngineType.MAVEN, PluginType.MAVEN_SOAPUI, EnvironmentType.QA), buildId)
                break
            case TaskType.LOAD_TESTING:
                stage = new EngineStage()
                stage.id = "load-testing"
                stage.name = "Load Testing"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.DEVELOPMENT, BranchType.RELEASE])
                break
            case TaskType.DEPLOY_PROD_LF:
                stage = new PlatformStage()
                stage.id = "deploy-prod-lf"
                stage.name = "Deploy Prod LF"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.RELEASE], false)
                stage.confirmation.enable = true
                break
            case TaskType.DEPLOY_PROD_T5:
                stage = new PlatformStage()
                stage.id = "deploy-prod-t5"
                stage.name = "Deploy Prod T5"
                stage.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.RELEASE], false)
                stage.confirmation.enable = true
                break
        }

        stage.init(init, buildId)

        return stage
    }
}
