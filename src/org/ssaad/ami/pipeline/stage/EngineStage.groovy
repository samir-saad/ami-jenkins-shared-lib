package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.engine.Engine
import org.ssaad.ami.pipeline.engine.EngineFactory

class EngineStage extends Stage {

    Engine engine

    @NonCPS
    @Override
    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
        this.engine = new EngineFactory().create(init, buildId)
    }

    @NonCPS
    @Override
    void customize(Map config) {
        super.customize(config)

        if (config?.engine != null)
            this.engine.customize(config.engine)
    }

    @Override
    void executeStage() {
        engine.execute()
    }
}
