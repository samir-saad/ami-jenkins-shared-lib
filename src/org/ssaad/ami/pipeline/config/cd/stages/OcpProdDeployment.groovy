package org.ssaad.ami.pipeline.config.cd.stages

import org.ssaad.ami.pipeline.config.objects.OcpDeployment
import org.ssaad.ami.pipeline.config.objects.Stage
import org.ssaad.ami.pipeline.config.objects.Template

class OcpProdDeployment extends OcpDeployment implements Serializable {

    Stage testing = new Stage()

    OcpProdDeployment() {
        super()

        this.name = "Deploy to Prod"
        this.project = "ag-pssg-is-prod"
        this.imageTag = '${config.app.version}'

        this.env = "prod"
        this.replicas = 2

        // Test params - no testing
        this.testing.enable = false
    }
}
