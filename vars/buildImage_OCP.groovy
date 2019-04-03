#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig

def call(PipelineConfig config) {

	println( "Build Image on " + config.cdConfig.containerBuildStage.cloudProvider + " started")

    String appJar = config.app.id + "/target/" + config.app.id + "-" + config.app.version + "-bin.jar"
    println(appJar)

    sh "rm -rf oc-build && mkdir -p oc-build/deployments"
    sh "cp ${appJar} oc-build/deployments/"

    // Create build and image streams
    sh "oc new-build --name=${config.app.id} ${config.cdConfig.containerBuildStage.baseImage} --binary=true  --labels=app=${config.app.id} -n ${config.cdConfig.ocpDevDeployment.project} || true"

    // Start the build
    sh "oc start-build ${config.app.id} --from-dir=oc-build/deployments --wait=true -n ${config.cdConfig.ocpDevDeployment.project} "
}
