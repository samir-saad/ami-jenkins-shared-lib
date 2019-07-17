package org.ssaad.ami.pipeline.engine

import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.EnginesEnum
import org.ssaad.ami.pipeline.common.Executable
import org.ssaad.ami.pipeline.common.Initializable

abstract class Engine implements Serializable, Initializable, Executable {

    String id
    String name
    EnginesEnum type
    // maven, gradle, npm, etc.
    String configDir
    String credentialsId
    Credentials credentials
}
