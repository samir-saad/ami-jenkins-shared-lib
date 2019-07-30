package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Deployment
import org.ssaad.ami.pipeline.common.DeploymentFactory
import org.ssaad.ami.pipeline.platform.Platform
import org.ssaad.ami.pipeline.common.Template
import org.ssaad.ami.pipeline.common.TemplateFactory
import org.ssaad.ami.pipeline.platform.PlatformFactory

class PlatformStage extends EngineStage {

    Deployment deployment
    Platform platform
//    List<Template> templates
    Template template

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

//        if (config?.engine != null)
//            this.engine.customize(config.engine)
    }

    @Override
    void executeStage() {
//        engine.execute()
    }
}
