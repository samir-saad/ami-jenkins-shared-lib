package org.ssaad.ami.pipeline.common


import org.ssaad.ami.pipeline.stage.Stage
import org.ssaad.ami.pipeline.stage.StageFactory

class PipelineFactory implements AbstractFactory<Pipeline> {

    @Override
    Pipeline create(String type) {

        Stage build = ((StageFactory) FactoryProvider.getFactory("Stage")).create("Build")

        Pipeline pipeline = new Pipeline()
        pipeline.id = "maven"
        pipeline.stages = new TreeMap()

        pipeline.stages.put("Build", build)

        return pipeline
    }
}
