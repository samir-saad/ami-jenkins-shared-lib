package org.ssaad.ami.pipeline.engine;

import org.ssaad.ami.pipeline.AbstractFactory;

public class EngineFactory implements AbstractFactory<Engine> {

    @Override
    public Engine create(String type) {

        Maven maven = Maven.builder()
                .id("mavem")
                .name("Maven")
                .goals("clean package")
                .params("-DskipTests=true")
                .build();

        return maven;
    }
}
