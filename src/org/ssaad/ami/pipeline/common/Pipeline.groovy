package org.ssaad.ami.pipeline.common

import groovy.json.JsonBuilder
import org.ssaad.ami.pipeline.engine.Engine
import org.ssaad.ami.pipeline.engine.EngineInitialization
import org.ssaad.ami.pipeline.stage.Stage
import org.ssaad.ami.pipeline.stage.StageFactory

class Pipeline implements Serializable, Customizable, Executable {

    String id = ""
    String name = ""
    String buildId
    def steps
    Application app = new Application()
    ScmRepository primaryConfigRepo = new ScmRepository()
    // to override the default one
    ScmRepository secondaryConfigRepo

    // Stages
    List<Stage> stages = new ArrayList<>()

    void init(PipelineInitialization init) {
        this.id = init.id
        this.name = init.name
        this.buildId = init.buildId
        this.steps = init.steps
        this.app.scmType = init.scm
        this.primaryConfigRepo.scmType = init.scm
        this.primaryConfigRepo.url = "https://github.com/samir-saad/ami-pipeline-configs.git"
        this.primaryConfigRepo.branch = "master"
        this.primaryConfigRepo.localDir = "primaryConfigRepo"
        this.primaryConfigRepo.credentialsId = "ami-github"

        PipelineRegistry.registerPipeline(this)

        initStages(init.stageInitMap, init.buildId)
    }

    private void initStages(Map<TaskType, EngineInitialization> stageInitMap, String buildId){
        StageFactory stageFactory = new StageFactory()
        this.stages.add(stageFactory.create(TaskType.CODE_BUILD, stageInitMap.get(TaskType.CODE_BUILD), buildId))
//        this.stages.add(stageFactory.create(enginesEnum, TaskType.UNIT_TESTS))
//        this.stages.add(stageFactory.create(enginesEnum, TaskType.BINARIES_ARCHIVE))
    }

    @Override
    void customize(Map config) {

        if(config.primaryConfigRepo != null)
            this.primaryConfigRepo.customize(config.primaryConfigRepo)
        //Iniy secondary repo
    }

    @Override
    void execute() {

        def steps = PipelineRegistry.getPipelineSteps(buildId)

        steps.println("Execute pipeline stages")

        /*for (Stage currentStage : myPipeline.stages) {
            println("Current stage is: ${currentStage.name}")

            if (currentStage.isActive(myPipeline.app)) {
                println("Stage \"${currentStage.name}\" is active, execution will start shortly.")
                stage(currentStage.name) {
                    currentStage.execute(steps, myPipeline)
                }
            } else {
                println("Stage \"${currentStage.name}\" is inactive, execution is skipped.")
            }
        }*/
    }

    void print() {

        try {
            def steps = PipelineRegistry.getPipelineSteps(buildId)
            steps.println(new JsonBuilder(this).toPrettyString())

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
