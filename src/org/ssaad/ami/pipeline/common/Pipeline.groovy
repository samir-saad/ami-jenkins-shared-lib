package org.ssaad.ami.pipeline.common

import groovy.json.JsonBuilder
import org.ssaad.ami.pipeline.stage.Stage

class Pipeline implements Serializable, Customizable, Executable {

    String id = ""
    String name = " "
    Application app = new Application()
    ScmRepository primaryConfigRepo = new ScmRepository()
    // to override the default one
    ScmRepository secondaryConfigRepo

    // Stages
    List<Stage> stages = new ArrayList<>()

    void init(PipelineInitialization init) {
        this.id = init.id
        this.name = init.name
        this.app.scmType = init.scm
        this.primaryConfigRepo.scmType = init.scm
        this.primaryConfigRepo.url = "https://github.com/samir-saad/ami-pipeline-configs.git"
        this.primaryConfigRepo.branch = "master"
        this.primaryConfigRepo.localDir = "primaryConfigRepo"
        this.primaryConfigRepo.credentialsId = "ami-github"
    }

    @Override
    void customize(Map config) {

        //Iniy secondary repo
    }

    @Override
    void execute(steps, Pipeline myPipeline) {

        steps.println("Execute pipeline stages")

        steps.println(steps)
        steps.println(steps.myPipeline)

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

    void print(steps) {

        try {
            steps.println(new JsonBuilder(this).toPrettyString())
            steps.println(steps)
            steps.println(steps.myPipeline)

        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
