package org.ssaad.ami.pipeline.engine

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.stage.StageInitialization

class MavenFactory {

    @NonCPS
    Engine create(StageInitialization init) {

        Maven maven = new Maven()
        maven.id = "maven"
        maven.name = "Maven"
        maven.configDir = ""

        switch (init.taskType) {
            case TaskType.CODE_BUILD:
                maven.goals = "clean install"
                maven.params = "-DskipTests=true"
                break
            case TaskType.UNIT_TESTING:
                maven.goals = "test"
                break
            case TaskType.QUALITY_SCANNING:
                if (PluginType.MAVEN_SONAR_SCAN.equals(init.pluginType)) {
                    maven.configItemId = "sonarqube"
                    maven.goals = "sonar:sonar"
                    maven.params = "-Dsonar.projectKey=\${app.group}:\${app.id}:\${app.branch} " +
                            "-Dsonar.projectName=\"\${app.name} (\${app.branch})\" " +
                            "-DskipTests=true"
//                    maven.params = "-DskipTests=true -Dsonar.branch=\${app.branch}"
                }
                break
            case TaskType.DEPENDENCY_CHECK:
                if (PluginType.MAVEN_OWASP_DEPENDENCY_CHECK.equals(init.pluginType)) {
                    maven.goals = "dependency-check:check"
                    maven.params = "-Ddependency-check-maven.cveValidForHours=12 " +
                            "-Ddependency-check-maven.failBuildOnCVSS=4 " +
                            "-Ddependency-check-maven.suppressionFile=../primary-config-repo/build/maven/dependency-check-suppressions.xml"
                }
                break
            case TaskType.BINARIES_ARCHIVE:
                maven.credentialsId = "nexus-deployment"
                maven.goals = "deploy"
                maven.params = "-DskipTests=true " +
                        "-Drepository.deploy.username=\${engine.credentials.username} " +
                        "-Drepository.deploy.password=\${engine.credentials.password.plainText} "/* +
                        "-Dreleases-repository.url=http://nexus-cicd.cloudapps.ocp.local.net/repository/maven-releases/ " +
                        "-Dsnapshots-repository.url=http://nexus-cicd.cloudapps.ocp.local.net/repository/maven-snapshots/"*/
                break
            case TaskType.SYSTEM_TESTING:
                if (PluginType.MAVEN_SOAPUI.equals(init.pluginType)) {
                    maven.configDir = "src/test/soapui"
                    maven.goals = "com.smartbear.soapui:soapui-maven-plugin:5.4.0:test"
                    maven.params = "-Dsoapui.project=\${engine.configDir}/\${app.id}-soapui-project.xml " +
                            "-Dsoapui.properties=\${engine.configDir}/${init.environmentType.toString().toLowerCase()}.properties"
                }
                break
            case TaskType.LOAD_TESTING:
                if (PluginType.MAVEN_SOAPUI.equals(init.pluginType)) {
                    maven.configDir = "src/test/soapui"
                    maven.goals = "com.smartbear.soapui:soapui-maven-plugin:5.4.0:loadtest"
                    maven.params = "-Dsoapui.project=\${engine.configDir}/\${app.id}-soapui-project.xml " +
                            "-Dsoapui.properties=\${engine.configDir}/${init.environmentType.toString().toLowerCase()}.properties"
                }
                break
        }

        return maven
    }
}