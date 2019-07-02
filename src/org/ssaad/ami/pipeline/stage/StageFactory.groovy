package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.EnginesEnum
import org.ssaad.ami.pipeline.common.TasksEnum

class StageFactory {

    Stage create(EnginesEnum enginesEnum, TasksEnum task) {

        Stage stage
        switch (enginesEnum) {
            case EnginesEnum.MAVEN:
                stage = new EngineStage(enginesEnum, task)
                break
        }

        return stage
    }
}
