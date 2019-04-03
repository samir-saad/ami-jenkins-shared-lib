#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

    println("Fix App Directory")

    // Make app directory at parent
    sh "mkdir ../${config.app.id}"

    // Move all files to app directory
    sh "shopt -s dotglob nullglob; mv * ../${config.app.id}"

    // Bring app directory back to .
    sh "mv ../${config.app.id} ."
}