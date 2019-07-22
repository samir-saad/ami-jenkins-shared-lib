package org.ssaad.ami.pipeline.engine

import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.Executable
import org.ssaad.ami.pipeline.common.Customizable

abstract class Engine implements Serializable, Customizable, Executable {

    String id
    String name
    EngineType type
    // maven, gradle, npm, etc.
    String configDir
    String credentialsId
    Credentials credentials
}
