package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.TaskType
import org.ssaad.ami.pipeline.engine.EngineInitialization

class StageFactory {

    Stage create(TaskType task, EngineInitialization init, String buildId) {

        Stage stage
        switch (task) {
            case TaskType.INIT_PIPELINE:
                stage = new InitPipelineStage()
                break
            case TaskType.INIT_CONFIG:
                stage = new InitiConfigStage()
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

        stage.taskType = task
        stage.init(init, buildId)

        /*if(Engine != null){
            stage.engine = new EngineFactory().create(enginesEnum, task)
        }*/

        return stage
    }
}
