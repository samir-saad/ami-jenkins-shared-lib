package org.ssaad.ami.pipeline.common

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.engine.EngineInitialization

class PipelineTest {

    static void main(String[] args) {

        new PipelineTest().printPipeline()
    }

    void printPipeline(steps) {

        try {
            Pipeline pipeline = new Pipeline()
            PipelineInitialization initialization = new PipelineInitialization()
            initialization.id = "maven-spring-ocp-pipeline"
            initialization.name = "maven-spring-ocp-pipeline"
            initialization.buildId = "13"
            initialization.scm = ScmType.GIT
            initialization.stageInitMap.put(TaskType.CODE_BUILD, new EngineInitialization(EngineType.MAVEN, null))
            initialization.stageInitMap.put(TaskType.UNIT_TESTS, new EngineInitialization(EngineType.MAVEN, null))
            initialization.stageInitMap.put(TaskType.BINARIES_ARCHIVE, new EngineInitialization(EngineType.MAVEN, null))
            pipeline.init(initialization)

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
            // using Map to convert to Person object type
            pipeline.customize(new JsonSlurper().parseText(config))

        } catch (Exception e) {
            e.printStackTrace()
        }
    }

}
