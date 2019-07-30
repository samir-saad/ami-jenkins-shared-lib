package org.ssaad.ami.pipeline.platform

import org.ssaad.ami.pipeline.common.Platform
import org.ssaad.ami.pipeline.common.types.PlatformType
import org.ssaad.ami.pipeline.stage.*

class PlatformFactory {

    Platform create(StageInitialization init, String buildId) {

        Platform platform
        switch (init.platformType) {
            case PlatformType.OPENSHIFT:
                platform = new OpenShiftFactory().create(init, buildId)
                break
        }

        platform.init(init, buildId)

        return platform
    }
}
