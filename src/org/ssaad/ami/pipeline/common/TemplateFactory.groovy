package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.TemplateType
import org.ssaad.ami.pipeline.stage.StageInitialization
import org.ssaad.ami.pipeline.utils.TemplateUtils

class TemplateFactory {

    @NonCPS
    Template create(StageInitialization init, String buildId) {

        Template template
        switch (init.templateType) {
            case TemplateType.S2I_BUILD:
                template = TemplateUtils.getS2iBuildTemplate()
                break
            case TemplateType.SPRING_BOOT:
                template = TemplateUtils.getSpringBootTemplate()
                break
            case TemplateType.SPRING_CLOUD_CONFIG_SERVER:
                template = TemplateUtils.getSpringCloudConfigServerTemplate()
                break
            case TemplateType.SPRING_BOOT_WITH_CLOUD_CONFIG:
                template = TemplateUtils.getSpringBootWithCloudConfigTemplate()
                break
        }

//        if(EnvironmentType.DEV.equals(init.environmentType) && template.params.containsKey("IMAGE_TAG")){
//            template.params.put("IMAGE_TAG", "latest")
//        }

        template.init(init, buildId)

        return template
    }
}
