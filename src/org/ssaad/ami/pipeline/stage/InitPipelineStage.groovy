package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.BranchType
import org.ssaad.ami.pipeline.utils.PipelineUtils
import groovy.json.JsonSlurper

class InitPipelineStage extends Stage {

    @Override
    void executeStage() {

        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps
        def env = pipeline.env

        pipeline.app.branch = env.BRANCH_NAME

        if (pipeline.app.branch != null) {
            String branch = pipeline.app.branch.toUpperCase()
            if (branch.startsWith("MASTER"))
                pipeline.app.branchType = BranchType.MASTER
            else if (branch.startsWith("DEVELOP"))
                pipeline.app.branchType = BranchType.DEVELOP
            else if (branch.startsWith("FEATURE"))
                pipeline.app.branchType = BranchType.FEATURE
            else if (branch.startsWith("PR"))
                pipeline.app.branchType = BranchType.PR
            else if (branch.startsWith("RELEASE"))
                pipeline.app.branchType = BranchType.RELEASE
            else if (branch.startsWith("BUGFIX"))
                pipeline.app.branchType = BranchType.BUGFIX
            else if (branch.startsWith("HOTFIX"))
                pipeline.app.branchType = BranchType.HOTFIX
            else {
                steps.currentBuild.result = 'ABORTED'
                steps.error("Unrecognized branch type for branch: " + pipeline.app.branch)
            }

        }
        // Abort pipeline on master branch
        if ("master".equals(pipeline.app.branch)) {
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
            // using Map to convert to Person object appType
            pipeline.customize(new JsonSlurper().parseText(config))
        } else if (steps.fileExists('pipeline-config.yaml')) {
            // Do yaml init
        } else {
            steps.println("No init params")
        }

        //steps.println("Customized pipeline:")
//        pipeline.print()

        // Fix App dir
        // Make app directory at parent
        steps.sh "mkdir ../${pipeline.app.id}"
        // Move all files to app directory
        steps.sh "shopt -s dotglob nullglob; mv * ../${pipeline.app.id}"
        // Bring app directory back to .
        steps.sh "mv ../${pipeline.app.id} ."
    }
}
