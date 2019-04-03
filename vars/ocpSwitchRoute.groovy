#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Template

def call(PipelineConfig config, OcpDeployment ocpDeployment, String tag) {

	if(tag != null && !tag.empty) {

		for (Template t : ocpDeployment.templates) {
			if ("route".equals(t.type)) {

				t.params.put("OCP_OBJECT_NAME", t.params.get("OCP_OBJECT_NAME").replace("-green", ""))
				t.params.put("OCP_OBJECT_NAME", t.params.get("OCP_OBJECT_NAME").replace("-blue", ""))

				t.params.put("SERVICE_NAME", config.app.id + "-" + tag)

				ocpResolveParams(config, ocpDeployment, t)
				print("new Param: " + t.parsedParams)

				sh "oc process -f ${config.externalConfig.localDir}/${t.filePath} ${t.parsedParams} " +
						"| oc delete -f- -n ${ocpDeployment.project} || true"

				sh "oc process -f ${config.externalConfig.localDir}/${t.filePath} ${t.parsedParams} " +
						"-l app=${config.app.id}," +
						"app-version=${config.app.version}," +
						"deployment-type=${ocpDeployment.type}," +
						"code-commit=${config.app.latestCommit}," +
						"config-commit=${config.externalConfig.latestCommit} " +
						"| oc create -f- -n ${ocpDeployment.project} || true"
			}
		}
	}
}