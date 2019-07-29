package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.utils.JenkinsUtils
import org.ssaad.ami.pipeline.utils.PipelineUtils

class Maven extends Engine {

    String settingsFile = "/build/maven/settings.xml"
    String options = ""
    String goals = ""
    String params = ""

    @NonCPS
    @Override
    void customize(Map config) {
        super.customize(config)

        if (config?.settingsFile != null)
            this.settingsFile = config.settingsFile

        if (config?.options != null)
            this.options = config.options

        if (config?.goals != null)
            this.goals = config.goals

        if (config?.params != null)
            this.params = config.params
    }

    @Override
    void execute() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps

        if (credentialsId != null) {
            credentials = JenkinsUtils.getCredentials(credentialsId)
        }

        // Adjust settings
        steps.println("Config repo " + PipelineUtils.findConfigRepo(pipeline, settingsFile).toString())
        configRepo = PipelineUtils.findConfigRepo(pipeline, settingsFile)
        steps.println("Config repo " + configRepo.toString())
        steps.println(configRepo?.id)
        String settingsFileAbsolutePath = PipelineUtils.getFileAbsolutePath(pipeline, configRepo, settingsFile)

        String command = "mvn -s ${settingsFileAbsolutePath} ${this.options} ${this.goals} ${this.params}"

        command = PipelineUtils.resolveVars([engine: this], command)

        steps.sh(command)

    }
}
