package org.ssaad.ami.pipeline.common.openshift

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Customizable
import org.ssaad.ami.pipeline.stage.StageInitialization
import sun.security.krb5.internal.crypto.Nonce

class ImageStream implements Serializable, Customizable {

    String name
    String tag
    String image

    @NonCPS
    void init(StageInitialization init, String buildId) {
        String imageName = image.substring(image.lastIndexOf("/") + 1)
        this.name = imageName.substring(0, imageName.indexOf(":"))
        this.tag = imageName.substring(imageName.indexOf(":") + 1)
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.name != null)
            this.name = config.name

    }

}
