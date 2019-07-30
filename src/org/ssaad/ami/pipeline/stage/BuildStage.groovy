package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType

class BuildStage extends EngineStage {

    BuildStage() {
        this.id = "build"
        this.name = "Build"
    }

    @Override
    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
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
