package org.ssaad.ami.pipeline.engine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ssaad.ami.pipeline.Executable;
import org.ssaad.ami.pipeline.Initializable;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = Maven.class, name = "Maven")})

@Getter
@Setter
@AllArgsConstructor
public abstract class Engine implements Serializable, Initializable, Executable {

    private String id;
    private String name;
    // maven, gradle, npm, etc.
    private String configDir;
    private String credentials;
}
