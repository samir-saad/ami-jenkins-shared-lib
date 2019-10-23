package org.ssaad.ami.pipeline.platform

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.stage.StageInitialization

class OpenShiftFactory {

    @NonCPS
    OpenShift create(StageInitialization init, String buildId) {

        OpenShift openShift = new OpenShift()
        switch (init.environmentType) {
            case EnvironmentType.DEV:
                openShift.id = "openshift-dev"
                openShift.name = "OpenShift Dev"
                openShift.clusterId = "dev" // Set cluster id
                openShift.project = "pipeline-demo-dev"
                break
            case EnvironmentType.TEST:
                openShift.id = "openshift-test"
                openShift.name = "OpenShift TEST"
                openShift.clusterId = "test" // Set cluster id
                openShift.project = "pipeline-demo-test"
                break
            case EnvironmentType.PROD:
                openShift.id = "openshift-prod"
                openShift.name = "OpenShift Prod"
                openShift.clusterId = "prod" // Set cluster id
                openShift.project = "pipeline-demo-prod"
                break
        }

//        openShift.init(init, buildId)

        return openShift
    }
}
