package org.ssaad.ami.pipeline.common

class StageConfirmation implements Serializable, Customizable {

    boolean enable = false
    int time = 5
    String timeUnit = "MINUTES"
    String message = "Proceed?"
    String okOption = "Ok"

    @Override
    void customize(Map config) {
        if (config?.enable != null)
            this.enable = config.enable

        if (config?.time != null)
            this.time = config.time

        if (config?.timeUnit != null)
            this.timeUnit = config.timeUnit

        if (config?.message != null)
            this.message = config.message

        if (config?.okOption != null)
            this.okOption = config.okOption
    }
}
