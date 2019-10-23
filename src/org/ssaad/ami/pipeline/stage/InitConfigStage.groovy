package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.utils.JenkinsUtils

class InitConfigStage extends Stage {

    @Override
    void executeStage() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)

        // Clone primary repo
        JenkinsUtils.cloneScmRepo(pipeline.primaryConfigRepo, pipeline.steps)
        pipeline.steps.sh("ls -l")
        pipeline.steps.sh("ls -l ${pipeline.primaryConfigRepo.localDir}")

        // Clone secondary repo
        if (pipeline.secondaryConfigRepo != null)
            JenkinsUtils.cloneScmRepo(pipeline.secondaryConfigRepo, pipeline.steps)
    }
}
