package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Application
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.utils.PipelineUtils

class OpenshiftS2I extends Engine {

    String clusterId
    String project = "dev-ssaad"
    String baseImage = "registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.6"
    String appPackage = "/target/\${app.id}-\${app.version}-bin.jar"

    OpenshiftS2I() {
        this.id = "s2i"
        this.name = "S2I"
        this.configDir = ""
    }

    OpenshiftS2I(String buildId) {
        this.buildId = buildId
    }

    @NonCPS
    @Override
    void customize(Map config) {
    }

    @Override
    void execute() {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        Application app = pipeline.app
        def steps = pipeline.steps

        String binary = app.id + appPackage
        binary = PipelineUtils.resolveVars([app: app], binary)

        steps.println("Building container image for " + binary)

        steps.sh "rm -rf oc-build && mkdir -p oc-build/deployments"
        steps.sh "cp ${binary} oc-build/deployments/"

        String inputStreamImageName = baseImage.substring(baseImage.lastIndexOf("/") + 1)
        String inputStreamName = inputStreamImageName.substring(0, inputStreamImageName.indexOf(":"))
        String inputStreamTag = inputStreamImageName.substring(inputStreamImageName.indexOf(":") + 1)
        String buildName = "${app.id}-${app.version}".toLowerCase()

        String buildTemplate = "/build/s2i/build-template.yaml"
        configRepo = PipelineUtils.findConfigRepo(pipeline, buildTemplate)
        String templateFileAbsolutePath = PipelineUtils.getFileAbsolutePath(pipeline, configRepo, buildTemplate)

        if (clusterId != null) {
            steps.openshift.withCluster(clusterId) {
                build(steps, app, buildName, inputStreamName, inputStreamTag, templateFileAbsolutePath)
            }
        } else {
            steps.openshift.withCluster() {
                build(steps, app, buildName, inputStreamName, inputStreamTag, templateFileAbsolutePath)
            }
        }
    }

    private void build(steps, app, String buildName, String inputStreamName, String inputStreamTag, String templateFile) {
        steps.openshift.withProject(project) {
            steps.println('Creating a BuildConfig...')

            def template = steps.readYaml file: templateFile

            String params = "-p APP_NAME=${app.id} " +
                    "-p APP_VERSION=${app.version} " +
                    "-p BUILD_NAME=${buildName} " +
                    "-p INPUT_STREAM_NAME=${inputStreamName} " +
                    "-p INPUT_STREAM_TAG=${inputStreamTag} " +
                    "-p INPUT_STREAM_IMAGE=${baseImage}"


            def processed = steps.openshift.process(template, params)
            def created = steps.openshift.apply(processed)
            def bc = created.narrow('bc')
            steps.println('Starting a container build from the created BuildConfig...')
            def buildSelector = bc.startBuild("--from-dir=.", "--wait=true")
        }
    }
}
