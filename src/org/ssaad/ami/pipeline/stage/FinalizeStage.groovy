package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType

class FinalizeStage extends Stage {

    FinalizeStage() {
        this.id = "finalize"
        this.name = "Finalize"
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
    }

    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
    }

    @Override
    void executeStage() {
        PipelineRegistry.unregisterPipeline(buildId)
    }
}
