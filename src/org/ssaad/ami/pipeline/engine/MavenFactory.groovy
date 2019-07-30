package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.stage.StageInitialization

class MavenFactory {

    Engine create(StageInitialization init) {

        Maven maven = new Maven()
        maven.id = "maven"
        maven.name = "Maven"
        maven.configDir = ""

        switch (init.taskType) {
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
                maven.params = "-DskipTests=true -Dinternal.repo.username=\${engine.credentials.username} " +
                        "-Dinternal.repo.password=\${engine.credentials.password.plainText}"
                break
        }

        return maven
    }
}