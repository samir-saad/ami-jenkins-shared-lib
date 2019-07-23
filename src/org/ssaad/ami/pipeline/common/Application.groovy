package org.ssaad.ami.pipeline.common

class Application implements Serializable {

    String id
    String group
    String name
    String description
    String version
    String branch
    // service or library
    AppType type = AppType.APPLICATION
    String latestCommit
    ScmType scmType
}
