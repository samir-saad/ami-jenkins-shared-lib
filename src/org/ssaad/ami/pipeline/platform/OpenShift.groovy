package org.ssaad.ami.pipeline.platform

import com.cloudbees.groovy.cps.NonCPS
import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.Customizable
import org.ssaad.ami.pipeline.common.Platform
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.PlatformType
import org.ssaad.ami.pipeline.stage.StageInitialization

class OpenShift extends Platform {

    String clusterId
    String project

    OpenShift(){
        this.id = "openshift"
        this.name = "OpenShift"
    }

    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)

    }

    @NonCPS
    @Override
    void customize(Map config) {
        super.customize(config)

        if (config?.clusterId != null)
            this.clusterId = config.clusterId

        if (config?.project != null)
            this.project = config.project
    }
}
