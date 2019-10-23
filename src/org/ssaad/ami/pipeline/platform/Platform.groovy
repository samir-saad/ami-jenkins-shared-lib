package org.ssaad.ami.pipeline.platform

import com.cloudbees.groovy.cps.NonCPS
import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.Customizable
import org.ssaad.ami.pipeline.common.types.AppRuntimeType
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.PlatformType
import org.ssaad.ami.pipeline.stage.StageInitialization

abstract class Platform implements Serializable, Customizable {

    String id
    String name
    PlatformType platformType
    EnvironmentType environmentType
    AppRuntimeType appRuntimeType
    String credentialsId
    Credentials credentials

    @NonCPS
    void init(StageInitialization init, String buildId) {
        this.platformType = init.platformType
        this.environmentType = init.environmentType
        this.appRuntimeType = init.appRuntimeType
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.id != null)
            this.id = config.id

        if (config?.name != null)
            this.name = config.name

        if (config?.platformType != null)
            this.platformType = config.platformType as PlatformType

        if (config?.environmentType != null)
            this.environmentType = config.environmentType as EnvironmentType

        if (config?.appRuntimeType != null)
            this.appRuntimeType = config.appRuntimeType as AppRuntimeType

        if (config?.credentialsId != null)
            this.credentialsId = config.credentialsId
    }
}
