package org.ssaad.ami.pipeline.config.cd.stages

import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Template

class OcpDevDeployment extends OcpDeployment implements Serializable {

    SystemTesting testing = new SystemTesting()

    OcpDevDeployment() {
        super()

        this.name = "Deploy to Dev"
        this.project = "ag-pssg-is-dev"
        this.env = "dev"

        // Test params
        // Test params


        // Auto scaling
        this.autoScaling.enable = false
    }
}
