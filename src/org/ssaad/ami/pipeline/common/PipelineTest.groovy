package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.common.types.PipelineType
import org.ssaad.ami.pipeline.common.types.ScmType
import groovy.json.JsonSlurper

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

            Pipeline pipeline = new PipelineFactory().create(init)

            String config =
                    "{\n" +
                            "  \"primaryConfigRepo\": {\n" +
                            "    \"branch\": \"develop\"\n" +
                            "  },\n" +
                            "  \"stages\": [\n" +
                            "    {\n" +
                            "      \"taskType\": \"UNIT_TESTING\",\n" +
                            "      \"enable\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"taskType\": \"DEPLOY_DEV\",\n" +
                            "      \"deployment\": {\n" +
                            "        \"replicas\": 2,\n" +
                            "        \"autoScaling\": {\n" +
                            "          \"enable\": true\n" +
                            "        }\n" +
                            "      }\n" +
                            "    }\n" +
                            "  ]\n" +
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
