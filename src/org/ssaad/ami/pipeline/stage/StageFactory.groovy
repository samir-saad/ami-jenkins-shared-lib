package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.TaskType
import org.ssaad.ami.pipeline.engine.EngineFactory

class StageFactory {

    Stage create(EngineType enginesEnum, TaskType task) {

        Stage stage
        switch (task) {
            case TaskType.CODE_BUILD:
                stage = new EngineStage()
                stage.id = "build"
                stage.name = "Build"
                break
            case TaskType.UNIT_TESTS:
                stage = new UnitTestsStage()
                stage.id = "test"
                stage.name = "Test"
                break
            case TaskType.BINARIES_ARCHIVE:
                stage = new EngineStage()
                stage.id = "archive"
                stage.name = "Archive"
                break
        }

        stage.engine = new EngineFactory().create(enginesEnum, task)

        return stage
    }
}
