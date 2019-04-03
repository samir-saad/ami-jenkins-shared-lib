#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config, String env) {

	if("dev".equals(env)){
        return config.cdConfig.ocpDevDeployment.project
    } else if("test".equals(env)){
        return config.cdConfig.ocpTestDeployment.project
    } else if("prod".equals(env)){
        //return config.cdConfig.ocp.project
    }
}