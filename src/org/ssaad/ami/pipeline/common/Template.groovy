package org.ssaad.ami.pipeline.common

import com.cloudbees.groovy.cps.NonCPS
import org.ssaad.ami.pipeline.stage.StageInitialization

class Template implements Serializable, Customizable {

    String id
    String name
    String filePath
    String type
    Map<String, String> params = new HashMap<>()
    Map<String, String> extraParams = new HashMap<>()
    String parsedParams = ""

    Map<String, String> labels = new HashMap<>()
    Map<String, String> extraLabels = new HashMap<>()
    String parsedLabels = ""

    void init(StageInitialization init, String buildId) {
    }

    @NonCPS
    @Override
    void customize(Map config) {
        if (config?.id != null)
            this.id = config.id

        if (config?.name != null)
            this.name = config.name
    }
}
