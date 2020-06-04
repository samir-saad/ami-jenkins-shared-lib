package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import com.cloudbees.plugins.credentials.Credentials
import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType
import org.ssaad.ami.pipeline.common.types.TaskType

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
    Timeout stageTimeout = new Timeout()
    //StageInitialization initialization

    boolean isActive() {
        Application app = PipelineRegistry.getPipeline(buildId).app
        def steps = PipelineRegistry.getPipelineSteps(buildId)

        boolean appTypeAllowed = activation.allowedAppType.contains(AppType.ANY) || activation.allowedAppType.contains(app.appType)

        boolean branchAllowed = activation.allowedBranches.contains(BranchType.ANY) || activation.allowedBranches.contains(app.branchType)

        boolean allowSnapshot =
                (activation.allowSnapshot
                ||
                (!activation.allowSnapshot && !app?.version?.toUpperCase()?.contains("SNAPSHOT")))

        return enable && appTypeAllowed && branchAllowed && allowSnapshot
    }

    @NonCPS
    void init(StageInitialization init, String buildId) {
        //this.initialization = init
        this.buildId = buildId
        this.taskType = init.taskType
    }

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
                    steps.timeout(time: confirmation.timeout.time, unit: confirmation.timeout.unit) {
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
