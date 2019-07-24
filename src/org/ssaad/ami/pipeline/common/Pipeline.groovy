package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.engine.EngineInitialization
import org.ssaad.ami.pipeline.stage.Stage
import org.ssaad.ami.pipeline.stage.StageFactory
import org.ssaad.ami.pipeline.utils.PipelineUtils

class Pipeline implements Serializable, Customizable, Executable {

    String id = ""
    String name = ""
    String buildId
    def steps
    def env
    String workspaceDir
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
        this.env = init.env
        this.app.scmType = init.scm
        this.primaryConfigRepo.scmType = init.scm
        this.primaryConfigRepo.url = "https://github.com/samir-saad/ami-pipeline-configs.git"
        this.primaryConfigRepo.branch = "master"
        this.primaryConfigRepo.localDir = "primaryConfigRepo"
        this.primaryConfigRepo.credentialsId = "ami-github"

        PipelineRegistry.registerPipeline(this)

        initStages(init.stageInitMap, init.buildId)

    }

    private void initStages(Map<TaskType, EngineInitialization> stageInitMap, String buildId) {
        StageFactory stageFactory = new StageFactory()
//        this.stages.add(stageFactory.create(TaskType.INIT_PIPELINE, null, buildId))
        // init configs
        this.stages.add(stageFactory.create(TaskType.CODE_BUILD, stageInitMap.get(TaskType.CODE_BUILD), buildId))
//        this.stages.add(stageFactory.create(TaskType.UNIT_TESTS, stageInitMap.get(TaskType.UNIT_TESTS), buildId))
//        this.stages.add(stageFactory.create(TaskType.BINARIES_ARCHIVE, stageInitMap.get(TaskType.BINARIES_ARCHIVE), buildId))

//        if(stageInitMap.get(TaskType.CONTAINER_BUILD) != null)
//            this.stages.add(stageFactory.create(TaskType.CONTAINER_BUILD, stageInitMap.get(TaskType.CONTAINER_BUILD), buildId))

//        this.stages.add(stageFactory.create(TaskType.FINALIZE, null, buildId))
    }

//    void customizePipeline(String jsonConfig){
//        steps.println("Customize pipeline method started:")
//
//        Map config = new JsonSlurper().parseText(jsonConfig)
//
//        customize(config)
//
//        steps.println("Customized pipeline:")
//        print()
//    }

    @NonCPS
    @Override
    void customize(Map config) {

        // App
        if (config?.app != null)
            this.app.customize(config.app)

        if (config?.primaryConfigRepo != null)
            this.primaryConfigRepo.customize(config.primaryConfigRepo)

        //Init secondary repo
        if (config?.secondaryConfigRepo != null) {
            this.secondaryConfigRepo = new ScmRepository()
            this.secondaryConfigRepo.customize(config.secondaryConfigRepo)
        }

        //Stages
        if (config?.stages != null) {
            for (Map stageConfig : config.stages) {
                TaskType taskType = stageConfig.taskType
                Stage stage
                if(taskType != null){
                    stage = PipelineUtils.findStage(stages, taskType)
                    stage.customize(stageConfig)
                }
            }
        }
//        steps.println("Customized pipeline:")
//        print()
    }

    @Override
    void execute() {

        def steps = PipelineRegistry.getPipelineSteps(buildId)
        steps.println("Execute pipeline stages")
        for (Stage stage : stages) {
            stage.execute()
        }
    }

    void print() {
        try {
            steps.println(new JsonBuilder(this).toPrettyString())

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
