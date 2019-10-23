package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.EngineType
import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.stage.StageInitialization

class EngineFactory {

    @NonCPS
    Engine create(StageInitialization init, String buildId) {

        Engine engine
        switch (init.engineType) {
            case EngineType.MAVEN:
                engine = new MavenFactory().create(init)
                break
            case EngineType.OPENSHIFT:
                if (PluginType.OPENSHIFT_S2I.equals(init.pluginType)) {
                    engine = new OpenShiftS2I()
                } else if (PluginType.OPENSHIFT_DEPLOY.equals(init.pluginType)) {
                    engine = new OpenShiftDeploy()
                }
                break
        }

        engine.init(init, buildId)

        return engine
    }
}
