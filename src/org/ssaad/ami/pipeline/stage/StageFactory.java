package org.ssaad.ami.pipeline.stage;

import org.ssaad.ami.pipeline.AbstractFactory;
import org.ssaad.ami.pipeline.FactoryProvider;
import org.ssaad.ami.pipeline.Pipeline;
import org.ssaad.ami.pipeline.engine.Engine;
import org.ssaad.ami.pipeline.engine.EngineFactory;
import org.ssaad.ami.pipeline.engine.Maven;

public class StageFactory implements AbstractFactory<Stage> {

    @Override
    public Stage create(String type) {

        Engine maven = ((EngineFactory) FactoryProvider.getFactory("Engine")).create("Maven");
        EngineStage buildStage = EngineStage.builder()
                .id("build")
                .name("Build")
                .enable(true)
                .requiresConfirmation(false)
                .engine(maven)
                .build();

        return buildStage;
    }
}
