package org.ssaad.ami.pipeline.common


import org.ssaad.ami.pipeline.common.types.*
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineFactory {

    Pipeline create(PipelineInitialization init) {

        Pipeline pipeline = new Pipeline()
        switch (init.pipelineType) {
            case PipelineType.JAVA_LIB_MAVEN:
                pipeline.id = "java-lib-maven-pipeline"
                pipeline.name = "java-lib-maven-pipeline"

                init.stageInitList.add(new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
                init.stageInitList.add(new StageInitialization(TaskType.UNIT_TESTING, EngineType.MAVEN))
                init.stageInitList.add(new StageInitialization(TaskType.QUALITY_SCANNING, EngineType.MAVEN, PluginType.MAVEN_SONAR_SCAN))
                init.stageInitList.add(new StageInitialization(TaskType.DEPENDENCY_CHECK, EngineType.MAVEN, PluginType.MAVEN_OWASP_DEPENDENCY_CHECK))
                init.stageInitList.add(new StageInitialization(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN))

                break

            case PipelineType.SPRING_MAVEN_OPENSHIFT:
                pipeline.id = "maven-spring-ocp-pipeline"
                pipeline.name = "maven-spring-ocp-pipeline"

                init.stageInitList.add(new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
                init.stageInitList.add(new StageInitialization(TaskType.UNIT_TESTING, EngineType.MAVEN))
                init.stageInitList.add(new StageInitialization(TaskType.QUALITY_SCANNING, EngineType.MAVEN, PluginType.MAVEN_SONAR_SCAN))
                init.stageInitList.add(new StageInitialization(TaskType.DEPENDENCY_CHECK, EngineType.MAVEN, PluginType.MAVEN_OWASP_DEPENDENCY_CHECK))
                init.stageInitList.add(new StageInitialization(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN))

                init.stageInitList.add(new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT,
                        PluginType.OPENSHIFT_S2I, AppRuntimeType.JDK, EnvironmentType.DEV,
                        DeploymentType.BASIC, TemplateType.S2I_BUILD, PlatformType.OPENSHIFT))

                init.stageInitList.add(new StageInitialization(TaskType.DEPLOY_DEV, EngineType.OPENSHIFT,
                        PluginType.OPENSHIFT_DEPLOY, AppRuntimeType.JDK, EnvironmentType.DEV,
                        DeploymentType.BASIC, TemplateType.SPRING_BOOT_WITH_CLOUD_CONFIG, PlatformType.OPENSHIFT))

                init.stageInitList.add(new StageInitialization(TaskType.DEPLOY_QA, EngineType.OPENSHIFT,
                        PluginType.OPENSHIFT_DEPLOY, AppRuntimeType.JDK, EnvironmentType.QA,
                        DeploymentType.BLUE_GREEN, TemplateType.SPRING_BOOT_WITH_CLOUD_CONFIG, PlatformType.OPENSHIFT))

                init.stageInitList.add(new StageInitialization(TaskType.DEPLOY_PROD_LF, EngineType.OPENSHIFT,
                        PluginType.OPENSHIFT_DEPLOY, AppRuntimeType.JDK, EnvironmentType.PROD_LF,
                        DeploymentType.BLUE_GREEN, TemplateType.SPRING_BOOT_WITH_CLOUD_CONFIG, PlatformType.OPENSHIFT))

                init.stageInitList.add(new StageInitialization(TaskType.DEPLOY_PROD_T5, EngineType.OPENSHIFT,
                        PluginType.OPENSHIFT_DEPLOY, AppRuntimeType.JDK, EnvironmentType.PROD_T5,
                        DeploymentType.BLUE_GREEN, TemplateType.SPRING_BOOT_WITH_CLOUD_CONFIG, PlatformType.OPENSHIFT))

                break
        }

        pipeline.init(init)

        init.steps.println("Initial pipeline:")
        pipeline.print()

        return pipeline
    }
}
