package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.ScmType

class Application implements Serializable, Customizable {

    String id
    String group
    String name
    String description
    String version
    String branch
    // service or library
    AppType appType = AppType.APPLICATION
    String latestCommit
    ScmType scmType

    @NonCPS
    @Override
    void customize(Map config) {
        if (config.id != null)
            this.id = config.id

        if (config.group != null)
            this.group = config.group

        if (config.name != null)
            this.name = config.name

        if (config.description != null)
            this.description = config.description

        if (config.version != null)
            this.version = config.version

        if (config.appType != null)
            this.appType = config.appType as AppType

        if (config.scmType != null)
            this.scmType = config.scmType as ScmType
    }
}
