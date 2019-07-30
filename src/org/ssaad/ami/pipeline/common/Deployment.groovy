package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.DeploymentType
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.stage.StageInitialization

class Deployment implements Serializable, Customizable {

    DeploymentType deploymentType = DeploymentType.RECREATE
    EnvironmentType environmentType
    int replicas = 1
    AutoScaling autoScaling

    void init(StageInitialization init, String buildId) {
        this.deploymentType = init.deploymentType
        this.environmentType = init.environmentType
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.deploymentType != null)
            this.deploymentType = config.deploymentType

    }
}
