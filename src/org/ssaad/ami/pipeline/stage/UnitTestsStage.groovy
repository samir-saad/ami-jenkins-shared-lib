package org.ssaad.ami.pipeline.stage

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.AppType
import org.ssaad.ami.pipeline.common.types.BranchType

class UnitTestsStage extends EngineStage {

    UnitTestsStage() {
        this.id = "test"
        this.name = "Test"
    }

    @NonCPS
    @Override
    void init(StageInitialization init, String buildId) {
        super.init(init, buildId)
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
    }

    @Override
    void executeStage() {

        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        def steps = pipeline.steps
        steps.dir(pipeline.app.id) {
            engine.execute()

            //Archive test results
            if (steps.fileExists('**/target/surefire-reports/TEST-*.xml')) {
                steps.println("Archive surefire reports")
                steps.step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
            }
        }
    }
}
