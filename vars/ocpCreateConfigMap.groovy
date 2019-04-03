#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment, String configMapPath) {

    println("OCP Create Config Map: " + configMapPath)
    sh "oc process -f ${configMapPath} | oc delete -f- -n ${ocpDeployment.project} || true"
    sh "oc process -f ${configMapPath} -l commit=${config.externalConfig.latestCommit} " +
            "| oc create -f- -n ${ocpDeployment.project} || true"
}