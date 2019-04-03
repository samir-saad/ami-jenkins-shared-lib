#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment, String templatePath) {

    println("OCP Create Config Map: " + templatePath)

    sh "oc process -f ${templatePath} | oc delete -f- -n ${ocpDeployment.project} || true"
    sh "oc process -f ${templatePath} -p APP_NAME=${config.app.id} " +
            "-p BUILD_VERSION=${ocpDeployment.buildVersion} " +
            "-p IMAGE_NAME=${config.app.id} " +
            "-p IMAGE_TAG=${ocpDeployment.imageTag} " +
            "-p REPLICAS=${ocpDeployment.replicas} " +
            "-l app=${config.app.id},commit=${config.externalConfig.latestCommit} " +
            "| oc create -f- -n ${ocpDeployment.project} || true"
}
