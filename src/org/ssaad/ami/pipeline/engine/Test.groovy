package org.ssaad.ami.pipeline.engine

class Test {
    public static void main(String[] args) {
        String baseImage = "registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:1.6"
        String inputStreamImageName = baseImage.substring(baseImage.lastIndexOf("/") + 1)
        String inputStreamName = inputStreamImageName.substring(0, inputStreamImageName.indexOf(":"))
        String inputStreamTag = inputStreamImageName.substring(inputStreamImageName.indexOf(":") + 1)


        println(inputStreamImageName)

        println(inputStreamName)

        println(inputStreamTag)
    }
}
