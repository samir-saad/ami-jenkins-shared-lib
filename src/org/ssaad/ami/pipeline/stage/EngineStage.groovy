package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.engine.Engine

class EngineStage extends Stage {

    Engine engine

    @Override
    void customize(Map config) {
        engine.customize(config)
    }

    @Override
    void executeStage() {
        engine.execute()
    }
}
