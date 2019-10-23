package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.utils.JenkinsUtils
import org.ssaad.ami.pipeline.utils.MavenUtils
import org.ssaad.ami.pipeline.utils.PipelineUtils

class Maven extends Engine {

    String settingsFile = "/build/maven/settings.xml"
    String options = ""
    String goals = ""
    String params = ""

    String settingsFileRelativePath
    String command

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
        MavenUtils.execute(this)
    }
}
