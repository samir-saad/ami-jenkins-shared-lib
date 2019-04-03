package org.ssaad.ami.pipeline.config.objects

class Template implements Serializable {

    String name
    String filePath
    String type
    Map<String, String> params = new HashMap<>()
    String parsedParams
}
