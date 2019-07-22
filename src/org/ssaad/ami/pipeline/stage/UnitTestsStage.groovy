package org.ssaad.ami.pipeline.stage

import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry

class UnitTestsStage extends EngineStage {

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
