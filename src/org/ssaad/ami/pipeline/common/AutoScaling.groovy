package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.AutoScalingResourceType
import org.ssaad.ami.pipeline.stage.StageInitialization
import org.ssaad.ami.pipeline.utils.TemplateUtils

class AutoScaling implements Serializable, Customizable {

    boolean enable = true
    AutoScalingResourceType resourceType = AutoScalingResourceType.CPU
    int minReplicas = 2
    int maxReplicas = 4
    int thresholdPercentage = 70
    Template autoScalingTemplate = TemplateUtils.getHorizontalPodAutoscalerTemplate()
    // Initial delay for pods CPU to idle
    Timeout initialDelay = new Timeout(30, "SECONDS")


    void init(StageInitialization init, String buildId) {

    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.enable != null)
            this.enable = config.enable

        if (config?.resourceType != null)
            this.resourceType = config.resourceType as AutoScalingResourceType

        if (config?.minReplicas != null)
            this.minReplicas = config.minReplicas as Integer

        if (config?.maxReplicas != null)
            this.maxReplicas = config.maxReplicas as Integer

        if (config?.thresholdPercentage != null)
            this.thresholdPercentage = config.thresholdPercentage as Integer

        if (config?.autoScalingTemplate != null)
            this.autoScalingTemplate.customize(config.autoScalingTemplate)

        if (config?.initialDelay != null)
            this.initialDelay.customize(config.initialDelay)
    }
}
