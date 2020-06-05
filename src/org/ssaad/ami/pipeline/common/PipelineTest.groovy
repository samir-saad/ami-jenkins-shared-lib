package org.ssaad.ami.pipeline.common

import groovy.json.JsonSlurper
import org.ssaad.ami.pipeline.common.types.PipelineType
import org.ssaad.ami.pipeline.common.types.ScmType

class PipelineTest {

    static void main(String[] args) {

        new PipelineTest().printPipeline()
    }

    void printPipeline(steps) {

        try {
            PipelineInitialization init = new PipelineInitialization()
            init.pipelineType = PipelineType.JAVA_LIB_MAVEN
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
                            "      \"taskType\": \"QUALITY_SCANNING\",\n" +
                            "      \"enable\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"taskType\": \"DEPENDENCY_CHECK\",\n" +
                            "      \"enable\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"taskType\": \"BINARIES_ARCHIVE\",\n" +
                            "      \"enable\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"taskType\": \"DEPLOY_DEV\",\n" +
                            "      \"template\": {\n" +
                            "        \"id\": \"spring-cloud-config-server-template\",\n" +
                            "        \"extraParams\": {\n" +
                            "          \"IMAGE_TAG\": \"latest\"\n" +
                            "        }\n" +
                            "      },\n" +
                            "      \"platform\": {\n" +
                            "        \"clusterId\": \"ocp-dev\",\n" +
                            "        \"project\": \"pipeline-demo-dev\"\n" +
                            "      },\n" +
                            "      \"deployment\": {\n" +
                            "        \"deploymentType\": \"BASIC\"\n" +
                            "      }\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"taskType\": \"DEPLOY_TEST\",\n" +
                            "      \"template\": {\n" +
                            "        \"id\": \"spring-cloud-config-server-template\"\n" +
                            "      },\n" +
                            "      \"platform\": {\n" +
                            "        \"clusterId\": \"ocp-dev\",\n" +
                            "        \"project\": \"pipeline-demo-test\"\n" +
                            "      },\n" +
                            "      \"deployment\": {\n" +
                            "        \"deploymentType\": \"BASIC\"\n" +
                            "      }\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}"
            // using Map to convert to Person object appType
            pipeline.customize(new JsonSlurper().parseText(config))
//            pipeline.env = new org.jenkinsci.plugins.workflow.cps.EnvActionImpl()

            steps.println("Customized pipeline:")
//            PipelineUtils.toMap(pipeline.env)
            pipeline.print()

        } catch (Exception e) {
            e.printStackTrace()
        }
    }

}
