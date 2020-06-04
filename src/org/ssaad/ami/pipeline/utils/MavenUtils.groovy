package org.ssaad.ami.pipeline.utils

import org.ssaad.ami.pipeline.common.Application
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.common.PipelineRegistry
import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.engine.Maven
import org.ssaad.ami.pipeline.stage.EngineStage

class MavenUtils {

    static void execute(Maven maven) {
        Pipeline pipeline = PipelineRegistry.getPipeline(maven.buildId)
        def steps = pipeline.steps

        steps.dir(pipeline.app.id) {
            lookupCredentials(maven)
            lookupSettings(maven)
            formCommand(maven)

            switch (maven.taskType) {
                case TaskType.CODE_BUILD:
                    codeBuild(maven, steps)
                    break

                case TaskType.UNIT_TESTING:
                    unitTests(maven, steps)
                    break

                case TaskType.QUALITY_SCANNING:
                    qualityScanning(maven, steps)
                    break

                case TaskType.DEPENDENCY_CHECK:
                    dependencyCheck(maven, steps)
                    break

                case TaskType.BINARIES_ARCHIVE:
                    binariesArchive(maven, steps)
                    break

                case TaskType.SYSTEM_TESTING:
                    systemTesting(maven, steps)
                    break

                case TaskType.LOAD_TESTING:
                    loadTesting(maven, steps)
                    break
            }
        }
    }

    static void lookupCredentials(Maven maven) {
        if (maven.credentialsId != null) {
            maven.credentials = JenkinsUtils.getCredentials(maven.credentialsId)
        }
    }

    static void lookupSettings(Maven maven) {
        Pipeline pipeline = PipelineRegistry.getPipeline(maven.buildId)
        maven.configRepo = PipelineUtils.findConfigRepo(pipeline, maven.settingsFile)
        maven.settingsFileRelativePath = PipelineUtils.getFileRelativePath(pipeline, maven.configRepo, maven.settingsFile)
    }

    static void formCommand(Maven maven) {
        Pipeline pipeline = PipelineRegistry.getPipeline(maven.buildId)
        Application app = pipeline.app
        EngineStage stage = (EngineStage) pipeline.findStage(maven.taskType)

        String command = "mvn -s ${maven.settingsFileRelativePath} ${maven.options} ${maven.goals} ${maven.params}"
        maven.command = PipelineUtils.resolveVars(["engine": maven, "app": app, "stage": stage], command)
    }

    static void codeBuild(Maven maven, steps) {
        steps.sh(maven.command)
    }

    static void unitTests(Maven maven, steps) {
        steps.sh(maven.command)

        //Archive test results
        // TO DO parameterize. possible use of **/target for multi-module and target/**/sur... for nested dirs
        String surefireReports = "target/surefire-reports/TEST-*.xml"
        steps.println("Looking for surefire reports: ${surefireReports}")

        def files = steps.findFiles(glob: surefireReports)
        if (files.length > 0) {
            steps.println("Publishing surefire reports")
            steps.junit surefireReports
        }
    }

    static void qualityScanning(Maven maven, steps) {
        Pipeline pipeline = PipelineRegistry.getPipeline(maven.buildId)
        EngineStage stage = (EngineStage) pipeline.findStage(maven.taskType)

        if (PluginType.MAVEN_SONAR_SCAN.equals(maven.pluginType)) {
            steps.withSonarQubeEnv(maven.configItemId) {
                steps.sh(maven.command)

            }
            steps.timeout(time: stage.stageTimeout.time, unit: stage.stageTimeout.unit) {
                steps.waitForQualityGate(abortPipeline: true)
            }
        }
    }

    static void dependencyCheck(Maven maven, steps) {
        try {
            // TO DO possibly switch to Jenkins plugin
            steps.sh(maven.command)

        } catch (Exception e) {
            /*ByteArrayOutputStream baos = new ByteArrayOutputStream()
            PrintStream ps = new PrintStream(baos, true, "UTF-8")
            e.printStackTrace(ps)
            String data = new String(baos.toByteArray(), StandardCharsets.UTF_8)
            steps.println(data)*/
            steps.currentBuild.result = 'FAILURE'
            steps.error("Task ${maven.taskType} failed")

        } finally {
            // TO DO parameterize. possible use of **/target for multi-module and target/**/sur... for nested dirs
            String owaspReport = "target/dependency-check-report.xml"
            steps.println("Looking for dependency check report: ${owaspReport}")

            def files = steps.findFiles(glob: owaspReport)
            if (files.length > 0) {
                steps.println("Publishing dependency check report")
                steps.dependencyCheckPublisher pattern: owaspReport
            }
        }
    }

    static void binariesArchive(Maven maven, steps) {
        steps.sh(maven.command)
    }

    static void systemTesting(Maven maven, steps) {
        if (PluginType.MAVEN_SOAPUI.equals(maven.pluginType)) {
            steps.sh(maven.command)
        }
    }

    static void loadTesting(Maven maven, steps) {
        if (PluginType.MAVEN_SOAPUI.equals(maven.pluginType)) {
            steps.sh(maven.command)
        }
    }
}
