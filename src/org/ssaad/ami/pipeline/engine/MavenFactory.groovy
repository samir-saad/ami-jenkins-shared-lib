package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.PluginType
import org.ssaad.ami.pipeline.common.TaskType

class MavenFactory {

    Engine create(TaskType task, PluginType pluginType, String buildId) {

        Maven maven = new Maven()
        maven.id = "maven"
        maven.name = "Maven"
        maven.configDir = ""
        maven.plugin = pluginType
        maven.buildId = buildId

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
                maven.params = "-DskipTests=true -Dinternal.repo.username=\${engine.credentials.username} " +
                        "-Dinternal.repo.password=\${engine.credentials.password.value}"
                break
        }

        return maven
    }
}