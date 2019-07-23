package org.ssaad.ami.pipeline.stage

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.engine.EngineInitialization
import org.ssaad.ami.pipeline.utils.PipelineUtils

class InitPipelineStage extends Stage {

    InitPipelineStage(){
        this.id = "init-pipeline"
        this.name = "Init Pipeline"
    }

    void init(EngineInitialization init, String buildId){
        this.buildId = buildId
    }

    @Override
    void customize(Map config) {

    }

    @Override
    void executeStage() {

        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps

        // Abort pipeline on master branch
        if ("master".equals(BRANCH_NAME)){
            currentBuild.result = 'ABORTED'
            error("master branch isn't allowed")
        }

        // Workspace base directory
        pipeline.workspaceDir = "${WORKSPACE}"

        // App info
        steps.println("Get app info")
        PipelineUtils.completeAppInfo(pipeline.app, steps)
        pipeline.app.branch = "${BRANCH_NAME}"

        // Customize pipeline stages
        if (fileExists('pipeline-config.json')) {
            String config = readFile file: 'pipeline-config.json'
            steps.println("Custom Pipeline Params: \n" + config)
            // using Map to convert to Person object type
            pipeline.customize(new JsonSlurper().parseText(config))
        } else if (fileExists('pipeline-config.yaml')) {
            // Do yaml init
        } else {
            steps.println("No init params")
        }

        // Fix App dir
        // Make app directory at parent
        steps.sh "mkdir ../${pipeline.app.id}"
        // Move all files to app directory
        steps.sh "shopt -s dotglob nullglob; mv * ../${pipeline.app.id}"
        // Bring app directory back to .
        steps.sh "mv ../${pipeline.app.id} ."

        steps.println("Custom Pipeline: \n" + new JsonBuilder(pipeline).toPrettyString())
    }
}
