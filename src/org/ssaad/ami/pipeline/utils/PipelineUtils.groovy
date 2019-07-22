package org.ssaad.ami.pipeline.utils

class PipelineUtils {

    static void resolveVars(Map bindings, String varsString) {
        String sub
        int start = varsString.indexOf("\${")
        int end = varsString.indexOf("}")
        while (start != -1 && end != -1 && end > start) {
            sub = varsString.substring(varsString.indexOf("\${") + 2, varsString.indexOf("}"))

            String result = resolveVar(bindings, sub)

            varsString = varsString.replace("\${" + sub + "}", result)

            start = varsString.indexOf("\${")
            end = varsString.indexOf("}")
        }
    }

    private static resolveVar(Map bindings, String var) {
        Iterator it = var.tokenize(".").iterator()
        def object = bindings.get(it.next())

        while (it.hasNext() && object != null) {
            object = object.getProperties().get(it.next())
        }

        return object?.toString()
    }
}
