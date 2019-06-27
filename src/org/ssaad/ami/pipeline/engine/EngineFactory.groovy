package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.AbstractFactory

class EngineFactory implements AbstractFactory<Engine> {

    @Override
    Engine create(String type) {

        Maven maven = new Maven()
        maven.id = "mavem"
        maven.name = "Maven"
        maven.goals = "clean package"
        maven.params = "-DskipTests=true"

        return maven
    }
}
