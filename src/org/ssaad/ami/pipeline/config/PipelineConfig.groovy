
package org.ssaad.ami.pipeline.config

import org.ssaad.ami.pipeline.config.cd.CdConfig
import org.ssaad.ami.pipeline.config.ci.CiConfig
import org.ssaad.ami.pipeline.config.objects.Application

class PipelineConfig implements Serializable {

    String baseDir
    Application app = new Application()
    ExternalConfigRepo externalConfig = new ExternalConfigRepo()

    CiConfig ciConfig = new CiConfig()

    CdConfig cdConfig = new CdConfig()
}