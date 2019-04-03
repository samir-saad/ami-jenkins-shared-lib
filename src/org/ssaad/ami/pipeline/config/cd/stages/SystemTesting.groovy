package org.ssaad.ami.pipeline.config.cd.stages

import org.ssaad.ami.pipeline.config.objects.Stage

class SystemTesting extends Stage {

    SystemTesting() {
        this.enable = true
        this.id = "system-testing"
        this.name = "System Testing"
        this.engine = "maven"
        this.goals = "test"
    }
}
