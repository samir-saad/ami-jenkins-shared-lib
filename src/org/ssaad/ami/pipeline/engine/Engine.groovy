package org.ssaad.ami.pipeline.engine

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.ssaad.ami.pipeline.common.Executable
import org.ssaad.ami.pipeline.common.Initializable

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes([@JsonSubTypes.Type(value = Maven.class, name = "Maven")])

abstract class Engine implements Serializable, Initializable, Executable {

    String id
    String name
    // maven, gradle, npm, etc.
    String configDir
    String credentials
}
