package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Template
import org.ssaad.ami.pipeline.stage.StageInitialization
import org.ssaad.ami.pipeline.utils.OpenShiftUtils
import org.ssaad.ami.pipeline.utils.TemplateUtils

class OpenShiftDeploy extends OpenShift {

    String ocpSecretId = "image-pull-secret"
    Template imagePullSecretTemplate = TemplateUtils.getDockerHubSecretTemplate()

    OpenShiftDeploy() {
        this.id = "openshift-deploy"
        this.name = "OpenShift Deploy"
        this.configDir = ""
    }

    @NonCPS
    @Override
    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
    }

    @NonCPS
    @Override
    void customize(Map config) {
        super.customize(config)

        if (config?.id != null)
            this.id = config.id
    }

    @Override
    void executeEngine() {
        OpenShiftUtils.deploy(this)
    }

}
