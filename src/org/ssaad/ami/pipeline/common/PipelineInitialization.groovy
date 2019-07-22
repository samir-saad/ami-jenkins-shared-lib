package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineInitialization {

    String id = ""
    String name = ""
    ScmType scm
    List<StageInitialization> stageInitializationList
}
