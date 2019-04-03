#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Template

def call(PipelineConfig config, OcpDeployment ocpDeployment) {

    println("OCP config recreate")

    for (Template t : ocpDeployment.templates) {
        ocpResolveParams(config, ocpDeployment, t)

        sh "oc process -f ${config.externalConfig.localDir}/${t.filePath} ${t.parsedParams} " +
                "| oc delete -f- -n ${ocpDeployment.project} || true"

        sh "oc process -f ${config.externalConfig.localDir}/${t.filePath} ${t.parsedParams} " +
                "-l app=${config.app.id}," +
                "app-version=${config.app.version}," +
                "deployment-type=${ocpDeployment.type}," +
                "deployment-tag=${ocpDeployment.tag}," +
                "code-commit=${config.app.latestCommit}," +
                "config-commit=${config.externalConfig.latestCommit} " +
                "| oc create -f- -n ${ocpDeployment.project} || true"
    }
}