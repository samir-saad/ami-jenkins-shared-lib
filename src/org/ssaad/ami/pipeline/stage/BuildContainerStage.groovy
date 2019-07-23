package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.engine.EngineFactory
import org.ssaad.ami.pipeline.engine.EngineInitialization

class BuildContainerStage extends EngineStage {

    BuildContainerStage() {
        this.id = "build-container"
        this.name = "Build Container"
    }

    void init(EngineInitialization init, String buildId) {
        this.buildId = buildId
        this.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])

        this.engine = new EngineFactory().create(TaskType.CONTAINER_BUILD, init, buildId)
    }

    @Override
    void executeStage() {
        engine.execute()
    }
}
