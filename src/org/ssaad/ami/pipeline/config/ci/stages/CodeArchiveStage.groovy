package org.ssaad.ami.pipeline.config.ci.stages

import org.ssaad.ami.pipeline.config.objects.Stage

class CodeArchiveStage extends Stage implements Serializable {

    boolean enable = true
    String id = "archive"
    String name = ""
    String engine = "maven"
    String credentials = "ami-nexus"

    CodeArchiveStage() {
        // Stage vars
        this.enable = true
        this.id = "archive"
        this.name = "Archive"

        this.engine = "maven"
        this.goals = "deploy"
        this.params = "-DskipTests=true"
        this.credentials = "ami-nexus"
    }
}
