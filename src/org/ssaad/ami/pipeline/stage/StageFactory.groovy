package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.EnginesEnum
import org.ssaad.ami.pipeline.common.TasksEnum
import org.ssaad.ami.pipeline.engine.EngineFactory

class StageFactory {

    Stage create(EnginesEnum enginesEnum, TasksEnum task) {

        Stage stage = new EngineStage()
        switch (task) {
            case TasksEnum.BUILD:
                stage.id = "build"
                stage.name = "Build"
                break
            case TasksEnum.TEST:
                stage.id = "test"
                stage.name = "Test"
                break
            case TasksEnum.ARCHIVE:
                stage.id = "archive"
                stage.name = "Archive"
                break
        }

        stage.engine = new EngineFactory().create(enginesEnum, task)

        return stage
    }
}
