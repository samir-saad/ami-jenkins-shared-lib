package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.stage.PlatformStage

abstract class OpenShift extends Engine {

    String imagePushCredentialsId = "image-push-token"
    String imagePullCredentialsId = "image-pull-token"

    @Override
    void execute() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        PlatformStage stage = (PlatformStage) pipeline.findStage(taskType)
        org.ssaad.ami.pipeline.platform.OpenShift platform = (org.ssaad.ami.pipeline.platform.OpenShift) stage.platform
        def steps = pipeline.steps

        if (platform.clusterId != null && !platform.clusterId.trim().equals("")) {
            steps.openshift.withCluster(platform.clusterId) {
                executeEngine()
            }
        } else {
            steps.openshift.withCluster() {
                executeEngine()
            }
        }
    }

    abstract void executeEngine()

}
