package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType

class FinalizeStage extends Stage {

    FinalizeStage() {
        this.id = "finalize"
        this.name = "Finalize"
    }

    void init(StageInitialization init, String buildId){
        this.buildId = buildId
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
    }

    @Override
    void executeStage() {
        PipelineRegistry.unregisterPipeline(buildId)
    }
}
