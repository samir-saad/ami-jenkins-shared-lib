package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.TasksEnum

class MavenFactory {

    Engine create(TasksEnum task) {

        Maven maven = new Maven()
        maven.id = "maven"
        maven.name = "Maven"
        maven.configDir = ""

        switch (task) {
            case TasksEnum.BUILD:
                maven.goals = "clean install"
                maven.params = "-DskipTests=true"
                break
            case TasksEnum.TEST:
                maven.goals = "test"
                break
        }

        return maven
    }
}