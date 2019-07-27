package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.AppType
import org.ssaad.ami.pipeline.common.BranchType
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.engine.EngineInitialization
import org.ssaad.ami.pipeline.utils.JenkinsUtils

class InitiConfigStage extends Stage {

    InitiConfigStage(){
        this.id = "init-config"
        this.name = "Init Config"
    }

    void init(EngineInitialization init, String buildId){
        this.buildId = buildId
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
    }

    @Override
    void executeStage() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)

        // Clone primary repo
        JenkinsUtils.cloneScmRepo(pipeline.primaryConfigRepo, pipeline.steps)

        // Clone secondary repo
        if (pipeline.secondaryConfigRepo != null)
            JenkinsUtils.cloneScmRepo(pipeline.secondaryConfigRepo, pipeline.steps)
    }
}
