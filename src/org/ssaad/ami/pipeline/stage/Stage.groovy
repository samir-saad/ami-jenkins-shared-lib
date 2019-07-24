package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
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

    boolean isActive() {
        Application app = PipelineRegistry.getPipeline(buildId).app

        boolean appTypeAllowed = activation.allowedAppType.contains(AppType.ANY) || activation.allowedAppType.contains(app.type)

        String branch = app.branch.toUpperCase()
        if (branch != null && branch.indexOf("/") != -1)
            branch = branch.substring(0, branch.indexOf("/"))
        boolean branchAllowed = activation.allowedBranches.contains(BranchType.ANY) || activation.allowedBranches.contains(branch)

        return enable && appTypeAllowed && branchAllowed
    }

    abstract void init(EngineInitialization init, String buildId)

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.id != null)
            this.id = config.id

        if (config?.name != null)
            this.name = config.name

        if (config?.enable != null)
            this.enable = config.enable

        if (config?.credentialsId != null)
            this.credentialsId = config.credentialsId

        if (config?.activation != null)
            this.activation.customize(config.activation)

        if (config?.confirmation != null)
            this.confirmation.customize(config.confirmation)
    }

    @Override
    void execute() {
        def steps = PipelineRegistry.getPipelineSteps(buildId)
        steps.println("Current stage is: ${name}")

        if (isActive()) {
            steps.println("Stage \"${name}\" is active, execution will start shortly.")
            steps.stage(name) {
                //confirmation
                if (confirmation.enable) {
                    steps.timeout(time: confirmation.time, unit: confirmation.timeUnit) {
                        steps.input message: confirmation.message, ok: confirmation.okOption
                    }
                }
                executeStage()
            }
        } else {
            steps.println("Stage \"${name}\" is inactive, execution is skipped.")
        }
    }

    abstract void executeStage()
}
