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
            case EnvironmentType.QA:
                openShift.id = "openshift-qa"
                openShift.name = "OpenShift QA"
                openShift.clusterId = "qa" // Set cluster id
                openShift.project = "pipeline-demo-qa"
                break
            case EnvironmentType.PROD_LF:
                openShift.id = "openshift-prod-lf"
                openShift.name = "OpenShift Prod LF"
                openShift.clusterId = "prod-lf" // Set cluster id
                openShift.project = "pipeline-demo-prod-lf"
                break
            case EnvironmentType.PROD_T5:
                openShift.id = "openshift-prod-t5"
                openShift.name = "OpenShift Prod T5"
                openShift.clusterId = "prod-t5" // Set cluster id
                openShift.project = "pipeline-demo-prod-t5"
                break
        }

//        openShift.init(init, buildId)

        return openShift
    }
}
