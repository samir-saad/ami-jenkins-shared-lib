package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Deployment
import org.ssaad.ami.pipeline.common.DeploymentFactory
import org.ssaad.ami.pipeline.common.Template
import org.ssaad.ami.pipeline.common.TemplateFactory
import org.ssaad.ami.pipeline.platform.Platform
import org.ssaad.ami.pipeline.platform.PlatformFactory
import org.ssaad.ami.pipeline.utils.TemplateUtils

class PlatformStage extends EngineStage {

    Deployment deployment
    Platform platform
//    List<Template> templates
    Template template
    EngineStage testing

    @NonCPS
    @Override
    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
        this.deployment = new DeploymentFactory().create(init, buildId)
        this.platform = new PlatformFactory().create(init, buildId)
        this.template = new TemplateFactory().create(init, buildId)
    }

    @NonCPS
    @Override
    void customize(Map config) {
        super.customize(config)

        if (config?.deployment != null)
            this.deployment.customize(config.deployment)

        if (config?.platform != null)
            this.platform.customize(config.platform)

        if (config?.template != null) {
            if (config.template.id != null)
                this.template = TemplateUtils.getTemplateById(config.template.id)
            this.template.customize(config.template)
        }

        if (config?.testing != null && this.testing != null)
            this.testing.customize(config.testing)
    }

    @Override
    void executeStage() {
        engine.execute()
    }
}
