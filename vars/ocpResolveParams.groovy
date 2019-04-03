#!/usr/bin/groovy
import org.ssaad.ami.pipeline.config.PipelineConfig
import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Template

def call(PipelineConfig config, OcpDeployment ocpDeployment, Template template) {

    println("OCP resolve params")

    def bindings = [config: config, ocpDeployment: ocpDeployment]

    String param
    //for (Template t : ocpDeployment.templates) {
    String templateParams = ""
    for (String key : template.params.keySet()) {
        param = resolveParam(template.params.get(key), bindings)
        template.params.put(key, param)
        templateParams += " -p " + key + "=\"" + param + "\""
    }
    template.parsedParams = templateParams
    //}
}

def resolveParam(String param, Map binding) {
    String sub
    int start = param.indexOf("\${")
    int end = param.indexOf("}")
    while (start != -1 && end != -1 && end > start) {
        sub = param.substring(param.indexOf("\${") + 2, param.indexOf("}"))
        println(sub)
        String result = resolveVar(sub, binding)

        println(result)
        param = param.replace("\${" + sub + "}", result)

        start = param.indexOf("\${")
        end = param.indexOf("}")
    }
    return param
}

def resolveVar(String param, Map bindings) {
    Iterator it = param.tokenize(".").iterator()
    def object = bindings.get(it.next())

    while (it.hasNext() && object != null) {
        object = object.getProperties().get(it.next())
    }

    return object?.toString()
}