package org.ssaad.ami.pipeline.common

class Application implements Serializable {

    String id
    String group
    String name
    String description
    String version
    String branch
    // service or library
    AppType type
    String latestCommit
    ScmType scmType
}
