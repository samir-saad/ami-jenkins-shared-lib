package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.engine.Engine
import org.ssaad.ami.pipeline.engine.EngineInitialization

class EngineStage extends Stage {

    Engine engine

    @Override
    void init(EngineInitialization init, String buildId) {

    }

    @Override
    void customize(Map config) {
        engine.customize(config)
    }

    @Override
    void executeStage() {
        engine.execute()
    }
}
