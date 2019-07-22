package org.ssaad.ami.pipeline.stage


import org.ssaad.ami.pipeline.common.Pipeline

class UnitTestsStage extends EngineStage {

    @Override
    void execute(Object steps, Pipeline myPipeline) {

        dir(myPipeline.app.id) {
            engine.execute(steps, myPipeline)

            //Archive test reults
            if (fileExists('**/target/surefire-reports/TEST-*.xml')){
                step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
            }
        }

    }
}
