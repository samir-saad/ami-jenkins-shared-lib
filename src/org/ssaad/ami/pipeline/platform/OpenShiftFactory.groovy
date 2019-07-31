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
                openShift.clusterId = "" // Set cluster id
                openShift.project = "dev-ssaad"
                break
            case EnvironmentType.TEST:
                openShift.id = "openshift-test"
                openShift.name = "OpenShift Test"
                openShift.clusterId = "" // Set cluster id
                openShift.project = "test-ssaad"
                break
            case EnvironmentType.PROD:
                openShift.id = "openshift-prod"
                openShift.name = "OpenShift Prod"
                openShift.clusterId = "" // Set cluster id
                openShift.project = "prod-ssaad"
                break
        }

//        openShift.init(init, buildId)

        return openShift
    }
}
