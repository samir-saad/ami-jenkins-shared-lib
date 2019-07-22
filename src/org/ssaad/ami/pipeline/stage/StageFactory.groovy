package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.TaskType
import org.ssaad.ami.pipeline.engine.Engine
import org.ssaad.ami.pipeline.engine.EngineFactory
import org.ssaad.ami.pipeline.engine.EngineInitialization

class StageFactory {

    Stage create(TaskType task, EngineInitialization init, String buildId) {

        Stage stage
        switch (task) {
            case TaskType.CODE_BUILD:
                stage = new BuildStage()
                stage.init(init, buildId)
                break
            case TaskType.UNIT_TESTS:
                stage = new UnitTestsStage()
                stage.id = "test"
                stage.name = "Test"
                break
            case TaskType.BINARIES_ARCHIVE:
                stage = new EngineStage()
                stage.id = "archive"
                stage.name = "Archive"
                break
        }

        /*if(Engine != null){
            stage.engine = new EngineFactory().create(enginesEnum, task)
        }*/

        return stage
    }
}
