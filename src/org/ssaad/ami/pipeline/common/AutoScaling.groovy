package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.stage.StageInitialization

class AutoScaling implements Serializable, Customizable {

    boolean enable
    int minReplicas = 2
    int maxReplicas = 4
    int cpuThresholdPercentage = 70
    int memoryThresholdPercentage = 70


    void init(StageInitialization init, String buildId) {

    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.enable != null)
            this.enable = config.enable

    }
}
