#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment) {

    Map secretsMap = new HashMap()

    Map genericSecrets = ocpGetSecretAsMap(ocpDeployment.project, "application")
    Map appSpecificSecrets = ocpGetSecretAsMap(ocpDeployment.project, config.app.id)

    if(genericSecrets != null){
        secretsMap.putAll(genericSecrets)
    }

    if(appSpecificSecrets != null){
        secretsMap.putAll(appSpecificSecrets)
    }

    println(secretsMap)

    return secretsMap
}