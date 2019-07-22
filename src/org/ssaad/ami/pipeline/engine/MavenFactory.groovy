package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.TaskType

class MavenFactory {

    Engine create(TaskType task) {

        Maven maven = new Maven()
        maven.id = "maven"
        maven.name = "Maven"
        maven.configDir = ""

        switch (task) {
            case TaskType.CODE_BUILD:
                maven.goals = "clean install"
                maven.params = "-DskipTests=true"
                break
            case TaskType.UNIT_TESTS:
                maven.goals = "test"
                break
            case TaskType.BINARIES_ARCHIVE:
                maven.credentialsId = "ami-nexus"
                maven.goals = "deploy"
                maven.params = "-DskipTests=true -Dinternal.repo.username=deployment -Dinternal.repo.password=deployment123"
                break
        }

        return maven
    }
}