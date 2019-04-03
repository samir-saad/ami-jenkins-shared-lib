#!/usr/bin/groovy

import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(OcpDeployment ocpDeployment, String deploymentName) {
    println(ocpDeployment.secretsMap)
    if(!ocpDeployment.secretsMap.isEmpty()) {
        String envVarsString = ""
        for (String key : ocpDeployment.secretsMap.keySet()) {
            envVarsString += key.replace(".", "_").replace("-", "_").toUpperCase() + "=" + ocpDeployment.secretsMap.get(key).replace("\$", "\\\$") + " "
        }

        sh "oc set env dc/${deploymentName} ${envVarsString} -n ${ocpDeployment.project}"
    }
}