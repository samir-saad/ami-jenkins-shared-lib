package org.ssaad.ami.pipeline.stage

import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.engine.EngineInitialization

abstract class Stage implements Serializable, Customizable, Executable {

    String id
    String name
    String buildId
    TaskType taskType
    boolean enable = true
    Activation activation
    StageConfirmation confirmation = new StageConfirmation()
    String credentialsId
    Credentials credentials

    boolean isActive(){
        Application app = PipelineRegistry.getPipeline(buildId).app

        boolean appTypeAllowed = activation.allowedAppType.contains(AppType.ANY) || activation.allowedAppType.contains(app.type)

        boolean branchAllowed = activation.allowedBranches.contains(BranchType.ANY) ||
                activation.allowedBranches.contains(app.branch.substring(0, app.branch.indexOf("/")).toUpperCase())

        return enable && appTypeAllowed && branchAllowed
    }

    abstract void init(EngineInitialization init, String buildId)

    @Override
    void execute() {
        def steps = PipelineRegistry.getPipelineSteps(buildId)
        println("Current stage is: ${name}")

        if (isActive()) {
            steps.println("Stage \"${name}\" is active, execution will start shortly.")
            stage(name) {
                //confirmation
                if(confirmation.enable){
                    timeout(time:confirmation.time, unit:confirmation.timeUnit) {
                        input message: confirmation.message, ok: confirmation.okOption
                    }
                }
                executeStage()
            }
        } else {
            println("Stage \"${name}\" is inactive, execution is skipped.")
        }
    }

    abstract void executeStage()
}
