package org.ssaad.ami.pipeline.utils

import org.ssaad.ami.pipeline.config.PipelineConfig
import com.cloudbees.groovy.cps.NonCPS
import groovy.json.JsonSlurper

class PipelineUtils implements Serializable {

    @NonCPS
    public static PipelineConfig initConfig(String config){
        return new PipelineConfig(new JsonSlurper().parseText(config))
    }
}
