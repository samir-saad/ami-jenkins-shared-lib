package org.ssaad.ami.pipeline.engine


import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.utils.JenkinsUtils
import org.ssaad.ami.pipeline.utils.PipelineUtils

class Maven extends Engine {

    String settingsFile
    String options = ""
    String goals = ""
    String params = ""

    @Override
    void customize(Map config) {

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
        PipelineUtils.resolveVars(bindings, command)

        steps.sh(command)

    }
}
