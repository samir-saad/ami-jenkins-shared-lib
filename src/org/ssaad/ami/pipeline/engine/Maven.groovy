package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.EnginesEnum
import org.ssaad.ami.pipeline.common.TasksEnum

class Maven extends Engine {

    String settingsFile
    String options
    String goals
    String params

    Maven() {
        this.id = "maven"
        this.name = "Maven"
        this.type = EnginesEnum.MAVEN
        this.configDir = ""
    }

    Maven(TasksEnum task) {
        this()
        switch (task) {
            case TasksEnum.BUILD:
                this.goals = "clean install"
                this.params = "-DskipTests=true"
                break
            case TasksEnum.TEST:
                this.goals = "test"
                break
        }

    }

    @Override
    void init() {

    }

    @Override
    void execute() {

    }
}
