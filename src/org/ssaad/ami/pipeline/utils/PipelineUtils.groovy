package org.ssaad.ami.pipeline.utils

import org.ssaad.ami.pipeline.common.Application
import org.ssaad.ami.pipeline.common.ScmType

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

    static void completeAppInfo(Application app, steps){
        if (fileExists('pom.xml')) {
            app.id = readMavenPom().getArtifactId()
            app.group = readMavenPom().getGroupId()
            app.name = readMavenPom().getName()
            app.description = readMavenPom().getDescription()
            app.version = readMavenPom().getVersion()
        }

        if (app.scmType.equals(ScmType.Git)){
            app.latestCommit = steps.sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        }

    }
}
