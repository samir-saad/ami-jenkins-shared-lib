package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.engine.EngineFactory

class ArchiveStage extends EngineStage {

    ArchiveStage() {
        this.id = "archive"
        this.name = "Archive"
    }

    void init(StageInitialization init, String buildId){
        this.buildId = buildId
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.DEVELOP, BranchType.RELEASE])
        this.engine = new EngineFactory().create(TaskType.BINARIES_ARCHIVE, init, buildId)
    }

    @Override
    void executeStage() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps
        steps.dir(pipeline.app.id) {
            engine.execute()
        }
    }
}
