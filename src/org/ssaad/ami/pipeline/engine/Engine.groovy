package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.Executable
import org.ssaad.ami.pipeline.common.Customizable
import org.ssaad.ami.pipeline.common.PluginType

abstract class Engine implements Serializable, Customizable, Executable {

    String id
    String name
    String buildId
    EngineType type
    PluginType plugin
    // maven, gradle, npm, etc.
    String configDir
    String credentialsId
    Credentials credentials

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.id != null)
            this.id = config.id

        if (config?.name != null)
            this.name = config.name

        if (config?.type != null)
            this.type = config.type as EngineType

        if (config?.plugin != null)
            this.plugin = config.plugin as PluginType

        if (config?.configDir != null)
            this.configDir = config.configDir

        if (config?.credentialsId != null)
            this.credentialsId = config.credentialsId
    }
}
