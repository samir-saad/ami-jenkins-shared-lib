package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.engine.EngineFactory
import org.ssaad.ami.pipeline.engine.EngineInitialization

class FinalizeStage extends Stage {

    FinalizeStage() {
        this.id = "finalize"
        this.name = "Finalize"
    }

    void init(EngineInitialization init, String buildId){
        this.buildId = buildId
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
    }

    void customize(Map config) {
    }

    @Override
    void executeStage() {
        PipelineRegistry.unregisterPipeline(buildId)
    }
}
