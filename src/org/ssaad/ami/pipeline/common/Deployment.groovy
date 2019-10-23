package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.DeploymentType
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.stage.StageInitialization

class Deployment implements Serializable, Customizable {

    DeploymentType deploymentType = DeploymentType.BASIC
    EnvironmentType environmentType
    String deploymentTag = ""
    int replicas = 1
    Timeout readinessTimeout = new Timeout()
    AutoScaling autoScaling = new AutoScaling()
    Timeout switchTrafficTimeout = new Timeout(30, "MINUTES")
    Timeout releaseApprovalTimeout = new Timeout(30, "MINUTES")

    @NonCPS
    void init(StageInitialization init, String buildId) {
        this.deploymentType = init.deploymentType
        this.environmentType = init.environmentType
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.deploymentType != null)
            this.deploymentType = config.deploymentType as DeploymentType

        if (config?.environmentType != null)
            this.environmentType = config.environmentType as EnvironmentType

        if (config?.deploymentTag != null)
            this.deploymentTag = config.deploymentTag

        if (config?.replicas != null)
            this.replicas = config.replicas as Integer

        if (config?.readinessTimeout != null)
            this.readinessTimeout.customize(config.readinessTimeout)

        if (config?.autoScaling != null)
            this.autoScaling.customize(config.autoScaling)

        if (config?.switchTrafficTimeout != null)
            this.switchTrafficTimeout.customize(config.switchTrafficTimeout)

        if (config?.releaseApprovalTimeout != null)
            this.releaseApprovalTimeout.customize(config.releaseApprovalTimeout)
    }
}
