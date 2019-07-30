package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType

class BuildContainerStage extends PlatformStage {

    BuildContainerStage() {
        this.id = "build-container"
        this.name = "Build Container"
    }

    @Override
    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
        this.activation = Activation.getInstance([AppType.APPLICATION], [BranchType.DEVELOP, BranchType.RELEASE])
    }

    @Override
    void executeStage() {
        engine.execute()
    }
}
