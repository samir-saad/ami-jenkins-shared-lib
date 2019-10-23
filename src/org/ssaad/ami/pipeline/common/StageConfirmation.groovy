package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS

class StageConfirmation implements Serializable, Customizable {

    boolean enable = false
    Timeout timeout = new Timeout()
    String message = "Proceed?"
    String okOption = "Ok"

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.enable != null)
            this.enable = config.enable

        if (config?.timeout != null)
            this.timeout.customize(config.timeout)

        if (config?.message != null)
            this.message = config.message

        if (config?.okOption != null)
            this.okOption = config.okOption
    }
}
