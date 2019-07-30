package org.ssaad.ami.pipeline.common.openshift

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.AppRuntimeType
import org.ssaad.ami.pipeline.stage.StageInitialization

class ImageStreamFactory {

    @NonCPS
    ImageStream create(StageInitialization init, String buildId) {

        ImageStream imageStream = new ImageStream()
        switch (init.appRuntimeType) {
            case AppRuntimeType.JDK:
                imageStream.image = S2iImages.JDK_1_6
                break
        }

        imageStream.init(init, buildId)

        return imageStream
    }
}
