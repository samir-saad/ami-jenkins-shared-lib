package org.ssaad.ami.pipeline.platform

import com.cloudbees.groovy.cps.NonCPS

class OpenShift extends Platform {

    String clusterId
    String project

    OpenShift() {
        this.id = "openshift"
        this.name = "OpenShift"
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
