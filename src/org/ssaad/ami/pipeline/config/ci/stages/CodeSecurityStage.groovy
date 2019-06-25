package org.ssaad.ami.pipeline.config.ci.stages

import org.ssaad.ami.pipeline.config.objects.Stage

class CodeSecurityStage extends Stage implements Serializable {

    CodeSecurityStage() {
        // Stage vars
        this.enable = true
        this.id = "security"
        this.name = "Dependencies Scanning"
        this.engine = "maven"
        this.goals = "dependency-check:check"
        this.params = "-DskipTests=true -DfailOnError=false"
    }
}
