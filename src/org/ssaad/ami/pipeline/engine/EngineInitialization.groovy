package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.PluginType

class EngineInitialization {

    EngineType engineType
    PluginType pluginType

    EngineInitialization(EngineType engineType, PluginType pluginType) {
        this.engineType = engineType
        this.pluginType = pluginType
    }
}
