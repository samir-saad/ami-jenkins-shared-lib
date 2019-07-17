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
        StageFactory stageFactory = new StageFactory()
        pipeline.stages.add(stageFactory.create(enginesEnum, TasksEnum.BUILD))
        pipeline.stages.add(stageFactory.create(enginesEnum, TasksEnum.TEST))
        pipeline.stages.add(stageFactory.create(enginesEnum, TasksEnum.ARCHIVE))
    }
}
