package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.TaskType
import org.ssaad.ami.pipeline.engine.EngineFactory
import org.ssaad.ami.pipeline.engine.EngineInitialization

class BuildStage extends EngineStage {

    BuildStage() {
        this.id = "build"
        this.name = "Build"
    }

    void init(EngineInitialization init, String buildId){
        this.buildId = buildId
        this.engine = new EngineFactory().create(TaskType.CODE_BUILD, init, buildId)
    }

    @Override
    void executeStage() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        dir(pipeline.app.id) {
            engine.execute()
        }
    }
}
