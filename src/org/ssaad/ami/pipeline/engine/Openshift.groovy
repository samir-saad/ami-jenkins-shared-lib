package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Application
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.openshift.ImageStream
import org.ssaad.ami.pipeline.common.openshift.ImageStreamFactory
import org.ssaad.ami.pipeline.platform.OpenShift
import org.ssaad.ami.pipeline.stage.PlatformStage
import org.ssaad.ami.pipeline.stage.StageInitialization
import org.ssaad.ami.pipeline.utils.OpenShiftUtils

abstract class Openshift extends Engine {

    @Override
    void execute() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        PlatformStage stage = (PlatformStage) pipeline.findStage(taskType)
        OpenShift platform = (OpenShift) stage.platform
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
