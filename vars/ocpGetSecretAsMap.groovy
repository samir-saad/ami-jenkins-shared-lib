#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Template
import groovy.json.JsonSlurper

def call(String ocpProject, String secretName) {
    Map secretsMap
    openshift.withCluster() {
        openshift.withProject(ocpProject) {
            def secretSelector = openshift.selector("secret", secretName)
            if(secretSelector.exists()){
                def secretObject = secretSelector.object()
                println(secretObject)
                if(secretObject.data != null){
                    println(secretObject.data)
                    secretsMap = secretObject.data

                    println(secretsMap)
                }
            }
        }
    }
    return secretsMap
}