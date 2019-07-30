package org.ssaad.ami.pipeline.common

import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.common.types.*
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineTest {

    static void main(String[] args) {

        new PipelineTest().printPipeline()
    }

    void printPipeline(steps) {

        try {
//            Pipeline pipeline = new Pipeline()
//            pipeline.id = "maven-spring-ocp-pipeline"
//            pipeline.name = "maven-spring-ocp-pipeline"

            PipelineInitialization init = new PipelineInitialization()
            init.pipelineType = PipelineType.SPRING_MAVEN_OPENSHIFT
            init.buildId = "13"
            init.scm = ScmType.GIT

//            init.stageInitMap.put(TaskType.CODE_BUILD, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
//            init.stageInitMap.put(TaskType.UNIT_TESTS, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
//            init.stageInitMap.put(TaskType.BINARIES_ARCHIVE, new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN))
//
//            init.stageInitMap.put(TaskType.CONTAINER_BUILD, new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT,
//                    PluginType.OPENSHIFT_S2I, AppRuntimeType.JDK, EnvironmentType.DEV,
//                    DeploymentType.RECREATE, TemplateType.S2I_BUILD, PlatformType.OPENSHIFT))

//            init.addStageInit(new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN, null, null, null, null, null, null))
//            init.addStageInit(new StageInitialization(TaskType.UNIT_TESTS, EngineType.MAVEN, null, null, null, null, null, null))
//            init.addStageInit(new StageInitialization(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN, null, null, null, null, null, null))

//            init.addStageInit(new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT, PluginType.OPENSHIFT_S2I,
//                    AppRuntimeType.JDK, EnvironmentType.DEV,
//                    DeploymentType.RECREATE, TemplateType.S2I_BUILD, PlatformType.OPENSHIFT))

            Pipeline pipeline = new PipelineFactory().create(init)
//            pipeline.init(init)

            String config =
                    "{\n" +
                            "    \"stages\": [        \n" +
                            "        {\n" +
                            "            \"confirmation\": {\n" +
                            "                \"time\": 10,\n" +
                            "                \"enable\": true\n" +
                            "            },\n" +
                            "            \"taskType\": \"CODE_BUILD\",\n" +
                            "            \"activation\": {\n" +
                            "                \"allowedBranches\": [\n" +
                            "                    \"ANY\",\n" +
                            "                    \"FEATURE\"\n" +
                            "                ],\n" +
                            "                \"allowedAppType\": [\n" +
                            "                    \"ANY\",\n" +
                            "                    \"LIBRARY\"\n" +
                            "                ]\n" +
                            "            }\n" +
                            "        }\n" +
                            "    ]\n" +
                            "}"
            // using Map to convert to Person object appType
            pipeline.customize(new JsonSlurper().parseText(config))

            steps.println("Customized pipeline:")
            pipeline.print()

        } catch (Exception e) {
            e.printStackTrace()
        }
    }

}
