#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment) {


	try {
		ocpDeployment.buildVersion = config.app.version.toLowerCase().replace(".", "-")

		input id: 'Rollback', message: 'Is the realise fine? Proceed with the rollback?', ok: 'Rollback!'
		sh "oc patch route ${config.app.id} -p \'{"spec":{"to":{"name":"' + dest + '"}}}\' -n ${config.cdConfig.ocpTestDeployment.project} || true"
		sh "oc patch route ${config.app.id}-${ocpDeployment.buildversion} -p \'{"spec":{"to":{"name":"' + xy + '"}}}\' -n ${config.cdConfig.ocpTestDeployment.project} || true"
	} catch (error) {
		sh "oc delete pods,services -l name=${config.app.id}-${ocpDeployment.buildversion}"
	}
}