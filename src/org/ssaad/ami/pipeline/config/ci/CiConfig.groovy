

package org.ssaad.ami.pipeline.config.ci

import org.ssaad.ami.pipeline.config.ci.stages.CodeArchiveStage
import org.ssaad.ami.pipeline.config.ci.stages.CodeBuildStage
import org.ssaad.ami.pipeline.config.ci.stages.CodeQualityStage
import org.ssaad.ami.pipeline.config.ci.stages.CodeSecurityStage
import org.ssaad.ami.pipeline.config.ci.stages.CodeTestStage



class CiConfig implements Serializable {

    CodeBuildStage codeBuildStage = new CodeBuildStage()
    CodeTestStage codeTestStage = new CodeTestStage()
    CodeQualityStage codeQualityStage = new CodeQualityStage()
    CodeSecurityStage codeSecurityStage = new CodeSecurityStage()
    CodeArchiveStage codeArchiveStage = new CodeArchiveStage()
}