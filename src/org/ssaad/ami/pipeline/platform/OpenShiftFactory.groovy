package org.ssaad.ami.pipeline.platform


import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.stage.StageInitialization

class OpenShiftFactory {

    OpenShift create(StageInitialization init, String buildId) {

        OpenShift openShift = new OpenShift()
        switch (init.environmentType) {
            case EnvironmentType.DEV:
                openShift.id = "openshift-dev"
                openShift.name = "OpenShift Dev"
                openShift.clusterId = "" // Set cluster id
                openShift.project = "ssaad-dev"
                break
            case EnvironmentType.TEST:
                openShift.id = "openshift-test"
                openShift.name = "OpenShift Test"
                openShift.clusterId = "" // Set cluster id
                openShift.project = "ssaad-test"
                break
            case EnvironmentType.PROD:
                openShift.id = "openshift-prod"
                openShift.name = "OpenShift Prod"
                openShift.clusterId = "" // Set cluster id
                openShift.project = "ssaad-prod"
                break
        }

//        openShift.init(init, buildId)

        return openShift
    }
}
