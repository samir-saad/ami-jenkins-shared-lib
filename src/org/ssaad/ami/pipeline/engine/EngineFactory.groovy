package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.PluginType
import org.ssaad.ami.pipeline.common.TaskType

class EngineFactory {

    Engine create(TaskType task, EngineInitialization init, String buildId) {

        Engine engine
        switch (init.engineType) {
            case EngineType.MAVEN:
                engine = new MavenFactory().create(task, init.pluginType, buildId)
                break
        }

        engine.type = init.engineType
        engine.plugin = init.pluginType

        return engine
    }
}
