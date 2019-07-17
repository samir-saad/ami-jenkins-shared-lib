package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.TasksEnum

class MavenFactory {

    Engine create(TasksEnum task) {

        Maven maven = new Maven()
        maven.id = "maven"
        maven.name = "Maven"
        maven.configDir = ""
        maven.options = ""

        switch (task) {
            case TasksEnum.BUILD:
                maven.goals = "clean install"
                maven.params = "-DskipTests=true"
                break
            case TasksEnum.TEST:
                maven.goals = "test"
                break
            case TasksEnum.ARCHIVE:
                maven.goals = "deploy"
                maven.params = "-DskipTests=true -Dinternal.repo.username=deployment -Dinternal.repo.password=deployment123"
                break
        }

        return maven
    }
}