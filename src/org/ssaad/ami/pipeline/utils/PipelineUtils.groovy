package org.ssaad.ami.pipeline.utils

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.*
import org.ssaad.ami.pipeline.stage.Stage

class PipelineUtils {

    static String resolveVars(Map bindings, String varsString) {
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

        return varsString
    }

    private static resolveVar(Map bindings, String var) {
        Iterator it = var.tokenize(".").iterator()
        // Get root object
        def object = bindings.get(it.next())

        // Process children/operations
        String segment
        while (it.hasNext() && object != null) {
            segment = it.next()
            if ("lowerCase".equals(segment)) {
                object = object.toString().toLowerCase()
            } else if ("upperCase".equals(segment)) {
                object = object.toString().toUpperCase()
            } else if ("trim".equals(segment)) {
                object = object.toString().trim()
            } else if ("normalize".equals(segment)) {
                object = normalize(object.toString())
            } else {
                object = object.getProperties().get(segment)
            }
        }
        return object?.toString()
    }

    private String normalize(String str) {
        return str
                .trim()
                .replace(".", "-")
                .replace("/", "-")
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

    static Stage findStage(String buildId, TaskType taskType) {
        Pipeline pipeline = PipelineRegistry.getPipeline(buildId)
        return findStage(pipeline.stages, taskType)
    }

    @NonCPS
    static Stage findStage(List<Stage> stages, TaskType taskType) {
        for (Stage stage : stages) {
            if (stage.taskType.equals(taskType))
                return stage
        }
        return null
    }

    static ScmRepository getConfigRepo(Pipeline pipeline, String filePath) {
        ScmRepository configRepo
        // Check file in secondary repo
        configRepo = getConfigRepo(pipeline, pipeline.secondaryConfigRepo, filePath)

        if (configRepo == null) {
            configRepo = getConfigRepo(pipeline, pipeline.primaryConfigRepo, filePath)
        }

        return configRepo
    }

    static ScmRepository getConfigRepo(Pipeline pipeline, ScmRepository configRepo, String filePath) {
        if (configRepo != null) {
            String fileRelativePath = getFileRelativePath(pipeline, configRepo, filePath)
            if (pipeline.steps.fileExists(fileRelativePath))
                return configRepo
        }
        return null
    }

    static String getFileAbsolutePath(Pipeline pipeline, ScmRepository configRepo, String filePath) {
        return getFileAbsolutePath(pipeline.workspaceDir, configRepo.localDir, filePath)
    }

    static String getFileAbsolutePath(String workspaceDir, String localDir, String filePath) {
        return normalizePath(workspaceDir + "/" + localDir + "/" + filePath)
    }

    static String getFileRelativePath(Pipeline pipeline, ScmRepository configRepo, String filePath) {
        String workspaceDir = pipeline.workspaceDir
        String pwd = pipeline.steps.pwd()
        String relativePath = ""

        String dirs = pwd.replace(workspaceDir, "")

        for (int i = 0; i < dirs.tokenize("/").size(); i++) {
            relativePath += "../"
        }

        relativePath += configRepo.localDir + "/" + filePath
        return normalizePath(relativePath)
    }

    static String normalizePath(String path){
        return path.replace("//", "/").trim()
    }
}
