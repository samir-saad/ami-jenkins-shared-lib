package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.common.types.ScmType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineInitialization {

    String id = ""
    String name = ""
    String buildId
    ScmType scm
    def steps
    def env
    Map<TaskType, StageInitialization> stageInitMap = new HashMap<>()

    void addStageInit(StageInitialization stageInitialization){
        stageInitMap.put(stageInitialization.taskType, stageInitialization)
    }
}
