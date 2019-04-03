#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

    sh "oc delete hpa -l app=${config.app.id} -n ${config.cdConfig.ocpTestDeployment.project} || true"

    sh "oc process -f ${config.cdConfig.ocpTestDeployment.template} -l commit=${cicdCommit} | oc create -f- -n ${config.cdConfig.ocpTestDeployment.project} || true"

    deployImage project: {config.cdConfig.ocpTestDeployment.project}, version: ${config.app.version.toLowerCase().replace(".", "-")}, replicas: 1
}
