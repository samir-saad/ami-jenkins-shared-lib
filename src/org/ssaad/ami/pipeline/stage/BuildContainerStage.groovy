package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.engine.EngineFactory

class BuildContainerStage extends PlatformStage {

    BuildContainerStage() {
        this.id = "build-container"
        this.name = "Build Container"
    }

    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
        this.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])
        this.engine = new EngineFactory().create(init, buildId)
    }

    @Override
    void executeStage() {
        engine.execute()
    }
}
