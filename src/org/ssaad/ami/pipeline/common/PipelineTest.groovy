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
            pipeline.init(initialization)

            steps.println new JsonBuilder(pipeline).toPrettyString()

            String config = "{\"primaryConfigRepo\": {\n" +
                    "        \"branch\": \"develop\",\n" +
                    "    }}"
            // using Map to convert to Person object type
            //pipeline.customize(new JsonSlurper().parseText(config))

            //steps.println new JsonBuilder(pipeline).toPrettyString()


        } catch (Exception e) {
            e.printStackTrace()
        }
    }

}
