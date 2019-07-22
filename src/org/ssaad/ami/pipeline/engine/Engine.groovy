package org.ssaad.ami.pipeline.engine

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
}
