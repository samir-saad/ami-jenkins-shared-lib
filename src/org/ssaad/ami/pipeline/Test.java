package org.ssaad.ami.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void main(String[] args) {

        Pipeline pipeline = ((PipelineFactory) FactoryProvider.getFactory("Pipeline")).create("Maven");

        //System.out.println(pipeline.getConfigJson());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writeValueAsString(pipeline));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
