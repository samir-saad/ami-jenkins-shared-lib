package org.ssaad.ami.pipeline;

import org.ssaad.ami.pipeline.stage.Stage;
import org.ssaad.ami.pipeline.stage.StageFactory;

import java.util.TreeMap;

public class PipelineFactory implements AbstractFactory<Pipeline> {

    @Override
    public Pipeline create(String type) {

        Stage build = ((StageFactory) FactoryProvider.getFactory("Stage")).create("Build");

        Pipeline pipeline = Pipeline.builder()
                .id("maven")
                .stages(new TreeMap())
                .build();

        pipeline.stages.put("Build", build);

        return pipeline;
    }
}
