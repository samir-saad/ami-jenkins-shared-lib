package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.TaskType

class EngineFactory {

    Engine create(EngineType enginesEnum, TaskType task) {

        Engine engine
        switch (enginesEnum) {
            case EngineType.MAVEN:
                engine = new MavenFactory().create(task)
                break
        }

        engine.type = enginesEnum
        return engine
    }
}
