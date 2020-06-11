package org.ssaad.ami.pipeline.common.openshift

class S2iImages implements Serializable {

    static final JDK8_RHEL = "registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift:latest"
    static final JDK8_UBI = "registry.access.redhat.com/ubi8/openjdk-8:latest"
    static final JDK11_UBI = "registry.access.redhat.com/ubi8/openjdk-11:latest"
    static final FABRIC8_JAVA11 = "docker.io/fabric8/s2i-java:latest-java11"
    static final FUSE6_JAVA = "registry.access.redhat.com/jboss-fuse-6/fis-java-openshift:latest"
    static final FUSE7_JAVA = "registry.access.redhat.com/fuse7/fuse-java-openshift:latest"
}
