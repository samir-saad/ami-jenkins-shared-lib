package org.ssaad.ami.pipeline.config.ci.stages

import org.ssaad.ami.pipeline.config.objects.Stage

class CodeBuildStage extends Stage implements Serializable {

    CodeBuildStage() {
        // Stage vars
        this.enable = true
        this.id = "build"
        this.name = "Build"

        this.engine = "maven"
        this.goals = "clean install"
        this.params = "-DskipTests=true"
        this.configDir = "external-config/build/maven"
    }
}
