package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS

class Application implements Serializable, Customizable {

    String id
    String group
    String name
    String description
    String version
    String branch
    // service or library
    AppType type = AppType.APPLICATION
    String latestCommit
    ScmType scmType

    //@NonCPS
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

        if (config.type != null)
            this.type = config.type

        if (config.scmType != null)
            this.scmType = config.scmType
    }
}
