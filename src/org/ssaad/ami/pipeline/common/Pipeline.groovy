package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonOutput
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.stage.Stage
import org.ssaad.ami.pipeline.stage.StageFactory
import org.ssaad.ami.pipeline.stage.StageInitialization
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

    @NonCPS
    void init(PipelineInitialization init) {
        this.id = init.id
        this.name = init.name
        this.buildId = init.buildId
        this.steps = init.steps
        this.env = init.env
        this.app.scmType = init.scm
        this.primaryConfigRepo.scmType = init.scm
        this.primaryConfigRepo.id = "primary"
        this.primaryConfigRepo.url = "https://github.com/samir-saad/ami-pipeline-configs.git"
        this.primaryConfigRepo.branch = "master"
        this.primaryConfigRepo.localDir = "primary-config-repo"
        this.primaryConfigRepo.credentialsId = "ami-github"

        PipelineRegistry.registerPipeline(this)

        initStages(init.stageInitList, init.buildId)
    }

    @NonCPS
    private void initStages(List<StageInitialization> stageInitList, String buildId) {
        StageFactory stageFactory = new StageFactory()
        // Initialize
        this.stages.add(stageFactory.create(new StageInitialization(TaskType.INIT_PIPELINE, null), buildId))
        this.stages.add(stageFactory.create(new StageInitialization(TaskType.INIT_CONFIG, null), buildId))
        // init configs
        for (StageInitialization stageInitialization : stageInitList) {
            this.stages.add(stageFactory.create(stageInitialization, buildId))
        }
    }

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
            this.secondaryConfigRepo.id = "secondary"
            this.secondaryConfigRepo.branch = "master"
            this.secondaryConfigRepo.localDir = "secondary-config-repo"
            this.secondaryConfigRepo.credentialsId = "ami-github"
            this.secondaryConfigRepo.customize(config.secondaryConfigRepo)
        }

        //Stages
        if (config?.stages != null) {
            for (Map stageConfig : config.stages) {
                TaskType taskType = stageConfig.taskType
                Stage stage
                if (taskType != null) {
                    stage = findStage(taskType)
                    stage?.customize(stageConfig)
                }
            }
        }
    }

    @NonCPS
    Stage findStage(TaskType taskType) {
        return PipelineUtils.findStage(stages, taskType)
    }

    @Override
    void execute() {

        def steps = PipelineRegistry.getPipelineSteps(buildId)
        steps.println("Execute pipeline stages")
        for (Stage stage : stages) {
            stage.execute()
        }
    }

    @NonCPS
    void print() {
        try {
            //def generator = new DefaultJsonGenerator()
            steps.println(JsonOutput.toJson(this))
            //steps.println(new JsonBuilder(this).toPrettyString())

        } catch (Exception e) {
            steps.println("Pipeline print error")
            e.printStackTrace()
        }
    }

    void cleanup() {
        PipelineRegistry.unregisterPipeline(buildId)
        // TODO workspace cleanup
    }
}
