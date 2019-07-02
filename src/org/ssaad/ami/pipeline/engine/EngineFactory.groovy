package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.EnginesEnum
import org.ssaad.ami.pipeline.common.TasksEnum

class EngineFactory {

    Engine create(EnginesEnum enginesEnum, TasksEnum task) {

        Engine engine
        switch (enginesEnum) {
            case EnginesEnum.MAVEN:
                engine = new MavenFactory().create(task)
                break
        }

        engine.type = enginesEnum
        return engine
    }
}
