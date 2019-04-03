package org.ssaad.ami.pipeline.config

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

class PipelineConfigTest {

    public static void main(String[] args) {


        String oldOcpObjectParam = "OCP_OBJECT_NAME=\"pipeline-demo-app\""
        print("oldOcpObjectParam: " + oldOcpObjectParam)

        String newOcpObjectParam = "OCP_OBJECT_NAME=\"pipeline-demo-app-green\""
        print("newOcpObjectParam: " + newOcpObjectParam)

        String params = "-p MEMORY_REQUEST=\"256Mi\" -p CPU_LIMIT=\"1\" -p IMAGE_TAG=\"latest\" -p IMAGE_NAME=\"pipeline-demo-app\" -p APP_NAME=\"pipeline-demo-app\" -p REPLICAS=\"1\" -p JAVA_OPTIONS=\"-XshowSettings:vm -Xms256m -Xmx768m\" -p CPU_REQUEST=\"100m\" -p OCP_OBJECT_NAME=\"pipeline-demo-app\" -p MEMORY_LIMIT=\"768Mi\""
        print("old Param: " + params)


        params = params.replace(oldOcpObjectParam, newOcpObjectParam)

        print("new Param: " + params)
    }
}
