package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.openshift.ImageStream
import org.ssaad.ami.pipeline.common.openshift.ImageStreamFactory
import org.ssaad.ami.pipeline.stage.StageInitialization
import org.ssaad.ami.pipeline.utils.OpenShiftUtils

class OpenshiftS2I extends Openshift {

    ImageStream imageStream
    String appPackage = "/target/\${app.id}-\${app.version}-bin.jar"

    OpenshiftS2I() {
        this.id = "s2i"
        this.name = "S2I"
        this.configDir = ""
    }

    @NonCPS
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
    void executeEngine() {
        OpenShiftUtils.s2iBuild(this)
    }

}
