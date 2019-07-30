package org.ssaad.ami.pipeline.common


import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.TemplateType
import org.ssaad.ami.pipeline.stage.StageInitialization
import org.ssaad.ami.pipeline.utils.TemplateUtils

class TemplateFactory {

    Template create(StageInitialization init, String buildId) {

        Template template
        switch (init.templateType) {
            case TemplateType.S2I_BUILD:
                template = TemplateUtils.getS2iBuildTemplate()
                break
            case TemplateType.SPRING_BOOT:
                template = TemplateUtils.getS2iBuildTemplate()
                break
            case TemplateType.SPRING_BOOT_WITH_CLOUD_CONFIG:
                template = TemplateUtils.getS2iBuildTemplate()
                break
        }

        template.init(init, buildId)

        return template
    }
}
