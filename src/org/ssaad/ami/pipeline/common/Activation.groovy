package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS

class Activation implements Serializable, Customizable {

    List<AppType> allowedAppType
    List<BranchType> allowedBranches

    static Activation getInstance(List<AppType> appTypes, List<BranchType> branchTypes){
        Activation activation = new Activation()
        activation.allowedAppType = appTypes.toList()
        activation.allowedBranches = branchTypes.toList()
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
    }
}
