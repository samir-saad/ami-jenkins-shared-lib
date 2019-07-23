package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Activation
import org.ssaad.ami.pipeline.common.AppType
import org.ssaad.ami.pipeline.common.BranchType
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.TaskType
import org.ssaad.ami.pipeline.engine.EngineFactory
import org.ssaad.ami.pipeline.engine.EngineInitialization

class UnitTestsStage extends EngineStage {

    UnitTestsStage(){
        this.id = "test"
        this.name = "Test"
    }

    void init(EngineInitialization init, String buildId){
        this.buildId = buildId
        this.activation = Activation.getInstance([AppType.ANY], [BranchType.ANY])
        this.engine = new EngineFactory().create(TaskType.UNIT_TESTS, init, buildId)
    }

    @Override
    void executeStage() {

        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)

        dir(pipeline.app.id) {
            engine.execute()

            //Archive test reults
            if (fileExists('**/target/surefire-reports/TEST-*.xml')) {
                step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
            }
        }
    }
}
