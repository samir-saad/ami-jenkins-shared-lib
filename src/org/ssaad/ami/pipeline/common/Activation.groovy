package org.ssaad.ami.pipeline.common

class Activation {

    List<AppType> allowedAppType
    List<BranchType> allowedBranches

    static Activation getInstance(List<AppType> appTypes, List<BranchType> branchTypes){
        Activation activation = new Activation()
        activation.allowedAppType = appTypes.toList()
        activation.allowedBranches = branchTypes.toList()
        return activation
    }
}
