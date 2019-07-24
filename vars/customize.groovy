#!/usr/bin/groovy
import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.common.Pipeline
import org.ssaad.ami.pipeline.stage.Stage

def call(Pipeline myPipeline) {

    println("Customize pipeline")

    for (Stage currentStage : myPipeline.stages) {
        println("Current stage is: ${currentStage.name}")

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
        myPipeline.customize(new JsonSlurper().parseText(config))
        myPipeline.print()
    }
}