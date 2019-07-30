package org.ssaad.ami.pipeline.engine

import org.ssaad.ami.pipeline.common.types.EngineType
import org.ssaad.ami.pipeline.common.types.TaskType

class MavenTest {

    static void main(String[] args) {

        Maven maven = new EngineFactory().create(EngineType.MAVEN, TaskType.CODE_BUILD)
        maven.execute(null, null)
    }
}
