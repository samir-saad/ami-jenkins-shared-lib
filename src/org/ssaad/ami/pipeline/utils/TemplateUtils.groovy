package org.ssaad.ami.pipeline.utils

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Template

class TemplateUtils implements Serializable {

    @NonCPS
    static Map<String, String> getCommonParams() {
        Map<String, String> params = new HashMap<>()
        params.put("APP_NAME", '${app.id}')
        params.put("APP_VERSION", '${app.version}')
        params.put("NAMESPACE", '${platform.project}')

        return params
    }

    @NonCPS
    static Map<String, String> getDCCommonParams() {
        Map<String, String> params = getCommonParams()
        params.put("APP_PROFILE", '${deployment.env.lowerCase}')
        params.put("IMAGE_NAME", '${app.id}')
        params.put("IMAGE_TAG", '${app.version}')
        params.put("REPLICAS", '${deployment.replicas}')

        return params
    }

    @NonCPS
    static Template getS2iBuildTemplate() {
        Template template = new Template()
        template.id = "s2i-build-template"
        template.name = "s2i-build-template"
        template.type = "bc"
        template.filePath = "/build/s2i/build-template.yaml"
        template.params = getCommonParams()
        template.params.put("BUILD_NAME", '${app.id}-${app.version.lowerCase}')
        template.params.put("INPUT_STREAM_NAME", '${engine.imageStream.name}')
        template.params.put("INPUT_STREAM_TAG", '${engine.imageStream.tag}')
        template.params.put("INPUT_STREAM_IMAGE", '${engine.imageStream.image}')

        return template
    }
}
