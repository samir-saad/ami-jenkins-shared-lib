package org.ssaad.ami.pipeline.utils

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.Application
import org.ssaad.ami.pipeline.common.ScmType
import org.ssaad.ami.pipeline.common.TaskType
import org.ssaad.ami.pipeline.stage.Stage

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

    static void completeAppInfo(Application app, steps) {
        if (steps.fileExists('pom.xml')) {
            app.id = steps.readMavenPom().getArtifactId()
            app.group = steps.readMavenPom().getGroupId()
            app.name = steps.readMavenPom().getName()
            app.description = steps.readMavenPom().getDescription()
            app.version = steps.readMavenPom().getVersion()
        }

        if (app.scmType.equals(ScmType.GIT)) {
            app.latestCommit = steps.sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        }
    }

    @NonCPS
    static Stage findStage(List<Stage> stages, TaskType taskType) {
        for (Stage stage : stages) {
            if (stage.taskType.equals(taskType))
                return stage
        }
        return null
    }
}
