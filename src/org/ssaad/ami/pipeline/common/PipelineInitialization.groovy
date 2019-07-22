package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.engine.EngineInitialization

class PipelineInitialization {

    String id = ""
    String name = ""
    String buildId
    ScmType scm
    def steps
    Map<TaskType, EngineInitialization> stageInitMap = new HashMap<>()
}
