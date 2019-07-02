package org.ssaad.ami.pipeline.common


import org.ssaad.ami.pipeline.stage.StageFactory

class PipelineFactory {

    Pipeline create(PipelineEnum pipelineEnum) {

        Pipeline pipeline = new Pipeline()
        pipeline.id = "maven-pipeline"
        pipeline.stages = new TreeMap()

        switch (pipelineEnum) {
            case PipelineEnum.MAVEN_SPRING_OPENSHIFT:
                pipeline.stages.put("build", new StageFactory().create(EnginesEnum.MAVEN, TasksEnum.BUILD))
                break
        }

        return pipeline
    }
}
