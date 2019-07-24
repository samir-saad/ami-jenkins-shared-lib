package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.engine.Engine
import org.ssaad.ami.pipeline.engine.EngineInitialization

class EngineStage extends Stage {

    Engine engine

    @Override
    void init(EngineInitialization init, String buildId) {

    }

    //@NonCPS
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
