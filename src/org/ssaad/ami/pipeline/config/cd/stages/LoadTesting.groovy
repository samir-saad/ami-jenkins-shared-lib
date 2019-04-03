package org.ssaad.ami.pipeline.config.cd.stages

import org.ssaad.ami.pipeline.config.objects.Stage

class LoadTesting extends Stage {

    LoadTesting() {
        this.enable = true
        this.id = "load-testing"
        this.name = "Load Testing"
        this.engine = "maven"
        this.goals = "loadtest"
    }
}
