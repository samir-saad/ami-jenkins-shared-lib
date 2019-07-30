import hudson.model.*

import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineFactory
import org.ssaad.ami.pipeline.common.PipelineInitialization
import org.ssaad.ami.pipeline.common.types.PipelineType
import org.ssaad.ami.pipeline.common.types.ScmType

def call(Closure body) {

    Pipeline myPipeline

    pipeline {

        agent { node { label 'maven' } }
        //agent any

        stages {
            stage("Create Pipeline") {
                steps {
                    script {
                        //println("Pipeline Configs: \n" + new JsonBuilder(superPipeline).toPrettyString())
                        echo("Creating pipeline ...")

                        PipelineInitialization init = new PipelineInitialization()
                        init.pipelineType = PipelineType.SPRING_MAVEN_OPENSHIFT
                        init.buildId = "${BUILD_TAG}"
                        init.scm = ScmType.GIT
                        init.steps = this
                        init.env = env

                        myPipeline = new PipelineFactory().create(init)
                    }
                }
            }
            stage("Execute Pipeline") {
                steps {
                    script {
                        echo("Executing pipeline ...")
                        myPipeline.execute()
                    }
                }
            }
        }
    }
}
