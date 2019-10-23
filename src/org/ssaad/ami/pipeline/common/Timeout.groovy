package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS

class Timeout implements Serializable, Customizable {
    int time = 5
    String unit = "MINUTES"

    Timeout(){

    }

    Timeout(long time, String unit) {
        this.time = time
        this.unit = unit
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.time != null)
            this.time = config.time

        if (config?.unit != null)
            this.unit = config.unit
    }
}
