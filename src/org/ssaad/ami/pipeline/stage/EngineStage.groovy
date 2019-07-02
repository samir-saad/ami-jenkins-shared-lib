package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.engine.Engine

class EngineStage extends Stage {

    Engine engine

    @Override
    boolean isActive() {
        return true
    }

    @Override
    void init(Map config) {
        engine.init(config)
    }

    @Override
    void execute(steps) {
        engine.execute(steps)
    }
}
