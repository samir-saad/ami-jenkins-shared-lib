#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

    println("Code build maven started")

    dir(config.app.id) {
        sh "mvn ${config.ciConfig.codeBuildStage.goals} ${config.ciConfig.codeBuildStage.params}"
    }
}