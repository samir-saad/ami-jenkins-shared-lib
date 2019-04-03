package org.ssaad.ami.pipeline.config.ci.stages

import org.ssaad.ami.pipeline.config.objects.Stage

class CodeTestStage extends Stage implements Serializable {

    CodeTestStage() {
        // Stage vars
        this.enable = true
        this.id = "test"
        this.name = "Test"

        this.engine = "maven"
        this.goals = "test"
    }
}
