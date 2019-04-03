

package org.ssaad.ami.pipeline.config.cd

import org.ssaad.ami.pipeline.config.cd.stages.ContainerBuildStage
import org.ssaad.ami.pipeline.config.cd.stages.OcpDevDeployment
import org.ssaad.ami.pipeline.config.cd.stages.OcpProdDeployment
import org.ssaad.ami.pipeline.config.cd.stages.OcpTestDeployment


class CdConfig implements Serializable {

    ContainerBuildStage containerBuildStage = new ContainerBuildStage()
    OcpDevDeployment ocpDevDeployment = new OcpDevDeployment()
    OcpTestDeployment ocpTestDeployment = new OcpTestDeployment()
    OcpProdDeployment ocpProdDeployment = new OcpProdDeployment()
}