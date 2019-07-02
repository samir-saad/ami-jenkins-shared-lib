package org.ssaad.ami.pipeline.common

import com.fasterxml.jackson.core.JsonProcessingException
import groovy.json.JsonBuilder

class PipelineTest {

    static void main(String[] args) {

        new PipelineTest().printPipeline()
    }

    void printPipeline(steps) {

        Pipeline pipeline = new PipelineFactory().create(PipelineEnum.MAVEN_SPRING_OPENSHIFT)

        try {
            steps.println new JsonBuilder(pipeline).toPrettyString()

        } catch (JsonProcessingException e) {
            e.printStackTrace()
        }
    }

}
