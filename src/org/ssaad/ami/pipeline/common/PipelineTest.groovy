package org.ssaad.ami.pipeline.common


import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.common.types.AppRuntimeType
import org.ssaad.ami.pipeline.common.types.DeploymentType
import org.ssaad.ami.pipeline.common.types.EngineType
import org.ssaad.ami.pipeline.common.types.EnvironmentType
import org.ssaad.ami.pipeline.common.types.PipelineType
import org.ssaad.ami.pipeline.common.types.PlatformType
import org.ssaad.ami.pipeline.common.types.PluginType
import org.ssaad.ami.pipeline.common.types.ScmType
import org.ssaad.ami.pipeline.common.types.TaskType
import org.ssaad.ami.pipeline.common.types.TemplateType
import org.ssaad.ami.pipeline.stage.StageInitialization

class PipelineTest {

    static void main(String[] args) {

        new PipelineTest().printPipeline()
    }

    void printPipeline(steps) {

        try {

            PipelineInitialization init = new PipelineInitialization()
            init.pipelineType = PipelineType.SPRING_MAVEN_OPENSHIFT
            init.buildId = "13"
            init.scm = ScmType.GIT

            init.addStageInit(new StageInitialization(TaskType.CODE_BUILD, EngineType.MAVEN, null, null, null, null, null, null))
            init.addStageInit(new StageInitialization(TaskType.UNIT_TESTS, EngineType.MAVEN, null, null, null, null, null, null))
            init.addStageInit(new StageInitialization(TaskType.BINARIES_ARCHIVE, EngineType.MAVEN, null, null, null, null, null, null))

            init.addStageInit(new StageInitialization(TaskType.CONTAINER_BUILD, EngineType.OPENSHIFT, PluginType.OPENSHIFT_S2I,
                    AppRuntimeType.JDK, EnvironmentType.DEV,
                    DeploymentType.RECREATE, TemplateType.S2I_BUILD, PlatformType.OPENSHIFT))
            
            Pipeline pipeline = new PipelineFactory().create(init)

            steps.println("Initial pipeline:")
            pipeline.print()

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
