package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.AbstractFactory
import org.ssaad.ami.pipeline.common.FactoryProvider
import org.ssaad.ami.pipeline.engine.Engine
import org.ssaad.ami.pipeline.engine.EngineFactory

class StageFactory implements AbstractFactory<Stage> {

    @Override
    Stage create(String type) {

        Engine maven = ((EngineFactory) FactoryProvider.getFactory("Engine")).create("Maven")

        EngineStage buildStage = new EngineStage()
        buildStage.id = "build"
        buildStage.name = "Build"
        buildStage.enable = true
        buildStage.requiresConfirmation = false
        buildStage.engine = maven

        return buildStage
    }
}
