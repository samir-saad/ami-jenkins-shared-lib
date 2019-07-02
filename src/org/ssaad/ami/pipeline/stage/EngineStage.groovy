package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.EnginesEnum
import org.ssaad.ami.pipeline.common.TasksEnum
import org.ssaad.ami.pipeline.engine.Engine
import org.ssaad.ami.pipeline.engine.EngineFactory

class EngineStage extends Stage {

    Engine engine

    EngineStage() {
        super()
    }

    EngineStage(EnginesEnum enginesEnum, TasksEnum task) {
        this()
        switch (task) {
            case TasksEnum.BUILD:
                this.id = "build"
                this.name = "Build"
                break
            case TasksEnum.TEST:
                this.id = "test"
                this.name = "Test"
                break
        }

        this.engine = new EngineFactory().create(enginesEnum, task)
    }

    @Override
    boolean isActive() {
        return true
    }

    @Override
    void init() {
        engine.init()
    }

    @Override
    void execute() {
        engine.execute()
    }
}
