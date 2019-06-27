package org.ssaad.ami.pipeline.common

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonBuilder

class PipelineTest {

    static void main(String[] args) {

        new PipelineTest().printPipeline()
    }

    void printPipeline(steps) {

        Pipeline pipeline = ((PipelineFactory) FactoryProvider.getFactory("Pipeline")).create("Maven")
        ObjectMapper objectMapper = new ObjectMapper()
        try {
            steps.println objectMapper.writeValueAsString(pipeline)
            steps.println new JsonBuilder(pipeline).toPrettyString()


            println "XXXXXXXXXXXXXXXX"
            System.out.println("YYYYYYYYYYYYYYYYYYYY")
        } catch (JsonProcessingException e) {
            e.printStackTrace()
        }
    }

}
