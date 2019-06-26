package org.ssaad.ami.pipeline.test

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes([
    @JsonSubTypes.Type(value = Dog.class, name = "Dog"),

    @JsonSubTypes.Type(value = Cat.class, name = "Cat")]
)
abstract class Animal {
    String name
}
