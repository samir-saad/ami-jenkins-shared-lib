package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonBuilder
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.utils.JenkinsUtils
import org.ssaad.ami.pipeline.utils.PipelineUtils

class Maven extends Engine {

    String settingsFile
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
        def steps = PipelineRegistry.getPipelineSteps(buildId)

        if (credentialsId != null) {
            credentials = JenkinsUtils.getCredentials(credentialsId)
        }

        String command = "mvn ${this.options} ${this.goals} ${this.params}"

        //Resolve command vars
        Map bindings = new HashMap()
        bindings.put("engine", this)


        try {
            steps.println(new JsonBuilder(bindings).toPrettyString())

        } catch (Exception e) {
            e.printStackTrace()
        }

        command = PipelineUtils.resolveVars(bindings, command)

        steps.sh(command)

    }
}
