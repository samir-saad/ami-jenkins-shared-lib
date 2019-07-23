package org.ssaad.ami.pipeline.common

class PipelineRegistry {
    private static Map<String, Pipeline> pipelines = new HashMap<>()

    static void registerPipeline(Pipeline pipeline) {
        pipelines.put(pipeline.buildId, pipeline)
    }

    static void unregisterPipeline(String buildId) {
        pipelines.remove(buildId)
    }

    static Pipeline getPipeline(String buildId) {
        return pipelines.get(buildId)
    }

    static Object getPipelineSteps(String buildId) {
        return pipelines.get(buildId).steps
    }
}
