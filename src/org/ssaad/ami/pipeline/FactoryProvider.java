package org.ssaad.ami.pipeline;

import org.ssaad.ami.pipeline.engine.EngineFactory;
import org.ssaad.ami.pipeline.stage.StageFactory;

public class FactoryProvider {
    public static AbstractFactory getFactory(String choice) {

        if ("Pipeline".equalsIgnoreCase(choice)) {
            return new PipelineFactory();
        } else if ("Stage".equalsIgnoreCase(choice)) {
            return new StageFactory();
        } else if ("Engine".equalsIgnoreCase(choice)) {
            return new EngineFactory();
        }

        return null;
    }
}
