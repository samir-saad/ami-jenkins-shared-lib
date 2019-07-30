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

class OpenshiftS2I extends Engine {

    ImageStream imageStream
    String appPackage = "/target/\${app.id}-\${app.version}-bin.jar"

    OpenshiftS2I() {
        this.id = "s2i"
        this.name = "S2I"
        this.configDir = ""

    }

    @Override
    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
        this.imageStream = new ImageStreamFactory().create(init, buildId)
    }

    @NonCPS
    @Override
    void customize(Map config) {
        super.customize(config)

        if (config?.appPackage != null)
            this.appPackage = config.appPackage
    }

    @Override
    void execute() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        Application app = pipeline.app
        PlatformStage stage = (PlatformStage) pipeline.findStage(taskType)
        OpenShift platform = (OpenShift) stage.platform
        def steps = pipeline.steps

        if (platform.clusterId != null && !platform.clusterId.trim().equals("")) {
            steps.openshift.withCluster(platform.clusterId) {
                OpenShiftUtils.s2iBuild(this)
            }
        } else {
            steps.openshift.withCluster() {
                OpenShiftUtils.s2iBuild(this)
            }
        }
    }

}
