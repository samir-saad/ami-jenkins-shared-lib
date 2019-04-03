package org.ssaad.ami.pipeline.config.cd.stages

import org.ssaad.ami.pipeline.config.objects.OcpDeployment

class OcpTestDeployment extends OcpDeployment implements Serializable {

    LoadTesting testing = new LoadTesting()

    OcpTestDeployment() {
        super()

        this.name = "Deploy to Test"
        this.project = "ag-pssg-is-test"
        this.imageTag = '${config.app.version}'

        this.env = "test"
        this.replicas = 2

        // Test params
    }
}
