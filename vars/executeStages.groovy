#!/usr/bin/groovy
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.stage.Stage

def call(Pipeline myPipeline) {

    println("Execute pipeline stages")

    for (Stage currentStage : myPipeline.stages) {
        println("Current stage is: ${currentStage.name}")

        if (currentStage.active) {
            println("Stage \"${currentStage.name}\" is active, execution will start shortly.")
            stage(currentStage.name) {
                currentStage.execute(this)
            }
        } else {
            println("Stage \"${currentStage.name}\" is inactive, execution is skipped.")
        }
    }
}