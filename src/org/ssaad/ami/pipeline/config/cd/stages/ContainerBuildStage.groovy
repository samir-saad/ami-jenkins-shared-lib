package org.ssaad.ami.pipeline.config.cd.stages

import org.ssaad.ami.pipeline.config.objects.Stage

class ContainerBuildStage extends Stage implements Serializable {

    boolean localArtifact = true
    String cloudProvider = "ocp"
//    String baseImage = "registry.access.redhat.com/jboss-fuse-6/fis-java-openshift:2.0"
    String baseImage = "samirsaad/s2i-openjdk-18"

    ContainerBuildStage() {

        // Stage vars
        this.enable = true
        this.id = "containerBuild"
        this.name = "Container Build"
        this.engine = "s2i"
    }
}
