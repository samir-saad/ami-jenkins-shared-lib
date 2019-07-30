package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.engine.EngineFactory

class BuildStage extends EngineStage {

    BuildStage() {
        this.id = "build"
        this.name = "Build"
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
    }

    void init(StageInitialization init){
        super.init(init)

    }

    @Override
    void executeStage() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps
        steps.dir(pipeline.app.id) {
            engine.execute()
        }
    }
}
