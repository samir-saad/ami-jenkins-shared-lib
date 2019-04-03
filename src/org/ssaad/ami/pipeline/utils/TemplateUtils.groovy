package org.ssaad.ami.pipeline.utils

import org.ssaad.ami.pipeline.config.objects.Template
import com.cloudbees.groovy.cps.NonCPS

public class TemplateUtils implements Serializable {

// Common Params
    @NonCPS
    public static Map<String, String> getCommonParams() {
        Map<String, String> params = new HashMap<>()
        params.put("APP_NAME", '${config.app.id}')
        params.put("NAMESPACE", '${ocpDeployment.project}')
        params.put("OCP_OBJECT_NAME", '${config.app.id}')

        return params
    }

    @NonCPS
    public static Map<String, String> getDCCommonParams() {
        Map<String, String> params = getCommonParams()
        params.put("APP_PROFILE", '${ocpDeployment.env}')
        params.put("IMAGE_NAME", '${config.app.id}')
        params.put("IMAGE_TAG", '${ocpDeployment.imageTag}')
        params.put("REPLICAS", '${ocpDeployment.replicas}')

        return params
    }

    @NonCPS
    public static Map<String, String> getRouteCommonParams() {
        Map<String, String> params = getCommonParams()
        params.put("SERVICE_NAME", '${config.app.id}')
        params.put("PORT", "http")

        return params
    }

// Deployment Config templates
    public static Template getSpringAppDC() {
        Template template = new Template()
        template.name = "ami-spring-app-template"
        template.type = "dc"
        template.filePath = "deploy/ocp/templates/ami-spring-app-template.yaml"
        template.params = getDCCommonParams()
        template.params.put("CONFIGS_BRANCH", '${ocpDeployment.env}')

        return template
    }

    @NonCPS
    public static Template getSpringConfigServerDC() {
        Template template = new Template()
        template.name = "ami-spring-config-server-template"
        template.type = "dc"
        template.filePath = "deploy/ocp/templates/ami-spring-config-server-template.yaml"
        template.params = getDCCommonParams()
        template.params.put("APP_DOMAIN", "AMI")
        template.params.put("APP_LOB", "AMI")
        template.params.put("GIT_URI", 'https://github.com/samir-saad/ami-apps-configs.git')
        template.params.put("REPLICAS", "1")

        return template
    }


// Routes templates
    @NonCPS
    public static Template getExternalRoute() {
        Template template = new Template()
        template.name = "ami-route-template"
        template.type = "route"
        template.filePath = "deploy/ocp/templates/ami-route-template.yaml"
        template.params = getRouteCommonParams()

        return template
    }

    @NonCPS
    public static Template getExternalMgmtRoute() {
        Template template = getExternalRoute()
        template.params.put("OCP_OBJECT_NAME", '${config.app.id}-mgmt')
        template.params.put("PORT", "mgmt")
        template.params.put("PATH", "/actuator")

        return template
    }
}