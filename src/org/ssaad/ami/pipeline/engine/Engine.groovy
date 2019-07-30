package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.common.types.EngineType
import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.stage.StageInitialization

abstract class Engine implements Serializable, Customizable, Executable {

    String id
    String name
    String buildId
    TaskType taskType
    EngineType engineType
    PluginType pluginType
    // maven, gradle, npm, etc.
    String configDir
    String credentialsId
    Credentials credentials
    ScmRepository configRepo

    @NonCPS
    void init(StageInitialization init, String buildId) {
        this.buildId = buildId
        this.taskType = init.taskType
        this.engineType = init.engineType
        this.pluginType = init.pluginType
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.id != null)
            this.id = config.id

        if (config?.name != null)
            this.name = config.name

        if (config?.engineType != null)
            this.engineType = config.engineType as EngineType

        if (config?.plugin != null)
            this.pluginType = config.plugin as PluginType

        if (config?.configDir != null)
            this.configDir = config.configDir

        if (config?.credentialsId != null)
            this.credentialsId = config.credentialsId
    }
}
