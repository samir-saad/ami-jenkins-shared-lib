package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.common.types.CreationPolicyType
import org.ssaad.ami.pipeline.stage.StageInitialization

class Template implements Serializable, Customizable {

    String id
    String name
    String filePath
    String type
    CreationPolicyType creationPolicy
    Map<String, String> params = new HashMap<>()
    Map<String, String> extraParams = new HashMap<>()
    String parsedParams = ""

    Map<String, String> labels = new HashMap<>()
    Map<String, String> extraLabels = new HashMap<>()
    String parsedLabels = ""

    @NonCPS
    void init(StageInitialization init, String buildId) {
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.id != null)
            this.id = config.id

        if (config?.name != null)
            this.name = config.name

        if (config?.filePath != null)
            this.filePath = config.filePath

        if (config?.type != null)
            this.type = config.type

        if (config?.creationPolicy != null)
            this.creationPolicy = config.creationPolicy as CreationPolicyType

        if (config?.params != null){
            this.params.clear()
            this.params.putAll(config.params)
        }

        if (config?.labels != null){
            this.labels.clear()
            this.labels.putAll(config.labels)
        }

        if (config?.extraParams != null){
            this.params.putAll(config.extraParams)
        }
                
        if (config?.extraLabels != null){
            this.labels.putAll(config.extraLabels)
        }
    }
}
