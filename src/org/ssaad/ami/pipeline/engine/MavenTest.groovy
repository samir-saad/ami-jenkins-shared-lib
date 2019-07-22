package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.EngineType
import org.ssaad.ami.pipeline.common.TaskType

class MavenTest {

    static void main(String[] args) {

        Maven maven = new EngineFactory().create(EngineType.MAVEN, TaskType.CODE_BUILD)
        maven.execute(null, null)
    }
}
