package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.TaskType

class StageFactory {

    @NonCPS
    Stage create(StageInitialization init, String buildId) {

        Stage stage
        switch (init.taskType) {
            case TaskType.INIT_PIPELINE:
                stage = new InitPipelineStage()
                break
            case TaskType.INIT_CONFIG:
                stage = new InitConfigStage()
                break
            case TaskType.CODE_BUILD:
                stage = new BuildStage()
                break
            case TaskType.UNIT_TESTS:
                stage = new UnitTestsStage()
                break
            case TaskType.BINARIES_ARCHIVE:
                stage = new ArchiveStage()
                break
            case TaskType.CONTAINER_BUILD:
                stage = new BuildContainerStage()
                break
            case TaskType.FINALIZE:
                stage = new FinalizeStage()
                break
        }

        stage.init(init, buildId)

        return stage
    }
}
