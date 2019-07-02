package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.Executable
import org.ssaad.ami.pipeline.common.Initializable

abstract class Stage implements Serializable, Initializable, Executable {

    String id
    String name
    boolean enable
    boolean requiresConfirmation

    Stage(){
        this.enable = true
        this.requiresConfirmation = false
    }

    abstract boolean isActive()
}
