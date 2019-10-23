package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.common.types.PipelineType
import org.ssaad.ami.pipeline.common.types.ScmType
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineInitialization implements Serializable {

    String id = ""
    String name = ""
    PipelineType pipelineType
    String buildId
    ScmType scm
    def steps
    def env

    List<StageInitialization> stageInitList = new ArrayList<>()
}
