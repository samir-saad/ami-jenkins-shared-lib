package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.engine.Engine

class EngineStage extends Stage {

    Engine engine

    @Override
    void customize(Map config) {
        engine.customize(config)
    }

    @Override
    void execute(steps, Pipeline myPipeline) {
        engine.execute(steps, myPipeline)
    }
}
