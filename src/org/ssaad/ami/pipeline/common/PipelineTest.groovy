package org.ssaad.ami.pipeline.common

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonBuilder

class PipelineTest {

    static void main(String[] args) {

        Pipeline pipeline = ((PipelineFactory) FactoryProvider.getFactory("Pipeline")).create("Maven")
        new PipelineTest().printPipeline(pipeline)
    }

    void printPipeline(Pipeline pipeline){

        ObjectMapper objectMapper = new ObjectMapper()
        try {
            System.out.println(objectMapper.writeValueAsString(pipeline))
            println new JsonBuilder(pipeline).toPrettyString()
        } catch (JsonProcessingException e) {
            e.printStackTrace()
        }
    }

}
