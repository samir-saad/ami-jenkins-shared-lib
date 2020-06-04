package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS

class PipelineRegistry {
    private static Map<String, Pipeline> pipelines = new HashMap<>()

    @NonCPS
    static void registerPipeline(Pipeline pipeline) {
        pipelines.put(pipeline.buildId, pipeline)
    }

    @NonCPS
    static void unregisterPipeline(String buildId) {
        pipelines.remove(buildId)
    }

    @NonCPS
    static Pipeline getPipeline(String buildId) {
        return pipelines.get(buildId)
    }

    @NonCPS
    static Object getPipelineSteps(String buildId) {
        return pipelines.get(buildId).steps
    }
}
