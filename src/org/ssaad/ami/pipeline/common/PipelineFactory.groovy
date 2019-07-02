package org.ssaad.ami.pipeline.common


import org.ssaad.ami.pipeline.stage.StageFactory

class PipelineFactory {

    Pipeline create(PipelineEnum pipelineEnum) {

        Pipeline pipeline = new Pipeline()
        pipeline.stages = new TreeMap()

        switch (pipelineEnum) {
            case PipelineEnum.MAVEN_SPRING_OPENSHIFT:
                pipeline.id = "maven-spring-ocp-pipeline"
                pipeline.name = "maven-spring-ocp-pipeline"
                initStages(pipeline, EnginesEnum.MAVEN)
                break
        }

        return pipeline
    }

    private void initStages(Pipeline pipeline, EnginesEnum enginesEnum){
        pipeline.stages.put("build", new StageFactory().create(enginesEnum, TasksEnum.BUILD))
    }
}
