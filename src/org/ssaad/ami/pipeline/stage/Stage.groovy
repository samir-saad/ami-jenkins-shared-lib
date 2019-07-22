package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.AppType
import org.ssaad.ami.pipeline.common.Application
import org.ssaad.ami.pipeline.common.BranchType
import org.ssaad.ami.pipeline.common.Executable
import org.ssaad.ami.pipeline.common.Customizable

abstract class Stage implements Serializable, Customizable, Executable {

    String id
    String name
    boolean enable
    Activation activation
    boolean requiresConfirmation

    Stage(){
        this.enable = true
        this.requiresConfirmation = false
    }

    boolean isActive(Application app){
        boolean appTypeAllowed = activation.allowedAppType.contains(AppType.ANY) || activation.allowedAppType.contains(app.type)

        String branchType = app.branch.substring(0, app.branch.indexOf("/")).toUpperCase()
        boolean branchAllowed = activation.allowedBranches.contains(BranchType.ANY) || activation.allowedBranches.contains(branchType)

        return enable && appTypeAllowed && branchAllowed
    }
}
