package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.AppVersionType
import org.ssaad.ami.pipeline.common.types.BranchType

class StageActivation implements Serializable, Customizable {

    Activation include
    Activation exclude


    @NonCPS
    static StageActivation getInstance(List<AppType> appTypes, List<BranchType> branchTypes){
        StageActivation activation = new StageActivation()
        activation.allowedAppType = appTypes.toList()
        activation.allowedBranches = branchTypes.toList()
        return activation
    }

    @NonCPS
    static StageActivation getInstance(List<AppType> appTypes, List<BranchType> branchTypes, boolean allowSnapshot){
        StageActivation activation = getInstance(appTypes, branchTypes)
        activation.allowSnapshot = allowSnapshot
        return activation
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.allowedAppType != null){
            this.allowedAppType.clear()
            for (String element : config.allowedAppType)
                this.allowedAppType.add(element as AppType)
        }

        if (config?.allowedBranches != null){
            this.allowedBranches.clear()
            for (String element : config.allowedBranches)
                this.allowedBranches.add(element as BranchType)
        }

        if (config?.allowSnapshot != null)
            this.allowSnapshot = config.allowSnapshot
    }
}
