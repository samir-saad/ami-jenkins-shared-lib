package org.ssaad.ami.pipeline.stage


import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.engine.EngineInitialization
import org.ssaad.ami.pipeline.utils.PipelineUtils

class InitPipelineStage extends Stage {

    InitPipelineStage(){
        this.id = "init-pipeline"
        this.name = "Init Pipeline"
    }

    void init(EngineInitialization init, String buildId){
        this.buildId = buildId
        this.activation =Activation.getInstance([AppType.ANY], [BranchType.ANY])
    }

    @Override
    void executeStage() {

        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps
        def env = pipeline.env

        pipeline.app.branch = env.BRANCH_NAME

        // Abort pipeline on master branch
        if ("master".equals(pipeline.app.branch)){
            steps.currentBuild.result = 'ABORTED'
            steps.error("master branch isn't allowed")
        }

        // Workspace base directory
        pipeline.workspaceDir = env.WORKSPACE

        // App info
        steps.println("Get app info")
        PipelineUtils.completeAppInfo(pipeline.app, steps)

        // Customize pipeline stages
        if (steps.fileExists('pipeline-config.json')) {
            String config = steps.readFile file: 'pipeline-config.json'
            steps.println("Custom Pipeline Params: \n" + config)
            // using Map to convert to Person object type
            //pipeline.customize(new JsonSlurper().parseText(config))
            pipeline.customizePipeline(config)
        } else if (steps.fileExists('pipeline-config.yaml')) {
            // Do yaml init
        } else {
            steps.println("No init params")
        }

        steps.println("Customized pipeline:")
        pipeline.print()

        pipeline = PipelineRegistry.getPipeline(buildId)
        pipeline.print()

        // Fix App dir
        // Make app directory at parent
        steps.sh "mkdir ../${pipeline.app.id}"
        // Move all files to app directory
        steps.sh "shopt -s dotglob nullglob; mv * ../${pipeline.app.id}"
        // Bring app directory back to .
        steps.sh "mv ../${pipeline.app.id} ."
    }
}
