#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment

def call(PipelineConfig config, OcpDeployment ocpDeployment) {

	println("SoapUI tests started: ${ocpDeployment.testing.name} on ${ocpDeployment.env}")

	dir(config.app.id) {
		String soapuiProject = "src/test/soapui/" + config.app.id + "-soapui-project.xml"
		String soapuiProperties = "src/test/soapui/" + ocpDeployment.env + ".properties"

		if (fileExists(soapuiProject) && fileExists(soapuiProperties)) {
			// Load test properties
			Properties props = readProperties file: soapuiProperties

			// Add application secrets
			Map<String, String> appSecrets = ocpDeployment.secretsMap
			println(appSecrets)
			for (String key : appSecrets.keySet()) {
				props.put(key, appSecrets.get(key))
			}

			// Add test secrets
			Map<String, String> appTestSecrets = ocpDeployment.testSecretsMap
			println(appTestSecrets)
			for (String key : appTestSecrets.keySet()) {
				props.put(key, appTestSecrets.get(key))
			}

			// Prepare properties to be saved
			String properties = ""
			for (String key : props.keySet()) {
				properties += key + "=" + props.getProperty(key) + "\n"
			}
			println(properties)

			writeFile file: soapuiProperties, text: properties

			sh "cat ${soapuiProperties}"

			// Run tests
			String command = "mvn com.smartbear.soapui:soapui-maven-plugin:5.4.0:" + ocpDeployment.testing.goals + " -Dsoapui.project=" + soapuiProject + " -Dsoapui.properties=" + soapuiProperties
			sh command
		}
	}
}