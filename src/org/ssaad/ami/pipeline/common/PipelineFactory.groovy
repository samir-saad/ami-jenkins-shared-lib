package org.ssaad.ami.pipeline.common


import org.ssaad.ami.pipeline.stage.StageFactory

class PipelineFactory {

    Pipeline create(PipelineEnum pipelineEnum) {

        Pipeline pipeline = new Pipeline()

        switch (pipelineEnum) {
            case PipelineEnum.MAVEN_SPRING_OPENSHIFT:
                pipeline.id = "maven-spring-ocp-pipeline"
                pipeline.name = "maven-spring-ocp-pipeline"
                initStages(pipeline, EngineType.MAVEN)
                break
        }

        return pipeline
    }

    private void initStages(Pipeline pipeline, EngineType enginesEnum){
        StageFactory stageFactory = new StageFactory()
        pipeline.stages.add(stageFactory.create(enginesEnum, TaskType.CODE_BUILD))
        pipeline.stages.add(stageFactory.create(enginesEnum, TaskType.UNIT_TESTS))
        pipeline.stages.add(stageFactory.create(enginesEnum, TaskType.BINARIES_ARCHIVE))
    }
}
