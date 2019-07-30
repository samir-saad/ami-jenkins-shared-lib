package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.ScmType

class ScmRepository implements Serializable, Customizable {

    ScmType scmType
    String id
    String name
    String url = ""
    String branch = ""
    String credentialsId = ""
    String localDir = ""
    String latestCommit

    @NonCPS
    @Override
    void customize(Map config) {
        if (config.scmType != null)
            this.scmType = config.scmType

        if (config.id != null)
            this.id = config.id

        if (config.name != null)
            this.name = config.name

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
