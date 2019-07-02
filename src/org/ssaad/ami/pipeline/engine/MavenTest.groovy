package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.EnginesEnum
import org.ssaad.ami.pipeline.common.TasksEnum

class MavenTest {

    static void main(String[] args) {

        Maven maven = new EngineFactory().create(EnginesEnum.MAVEN, TasksEnum.BUILD)
        maven.execute(null)
    }
}
