package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.stage.StageInitialization

class DeploymentFactory {

    @NonCPS
    Deployment create(StageInitialization init, String buildId) {

        Deployment deployment = new Deployment()
        switch (init.environmentType) {
            case EnvironmentType.DEV:
                deployment.replicas = 1
                break
            case EnvironmentType.TEST:
                deployment.replicas = 2
                deployment.autoScaling = new AutoScaling()
                break
            case EnvironmentType.PROD:
                deployment.replicas = 2
                deployment.autoScaling = new AutoScaling()
                break
        }

        deployment.init(init, buildId)

        return deployment
    }
}
