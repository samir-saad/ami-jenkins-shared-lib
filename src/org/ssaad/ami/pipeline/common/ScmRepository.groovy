package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS

class ScmRepository implements Serializable, Customizable {

    ScmType scmType
    String url = ""
    String branch = ""
    String credentialsId = ""
    String localDir = ""

    @NonCPS
    @Override
    void customize(Map config) {
        if (config.scmType != null)
            this.scmType = config.scmType

        if (config.url != null)
            this.url = config.url

        if (config.branch != null)
            this.branch = config.branch

        if (config.credentialsId != null)
            this.credentialsId = config.credentialsId

        if (config.localDir != null)
            this.localDir = config.localDir

    }
}
