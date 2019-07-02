package org.ssaad.ami.pipeline.stage

enum StageType {

    BUILD('build', 'Build'),
    TEST('test', 'Test')

    final String id
    final String name

    StageType(String id, String name) {
        this.id = id
        this.name = name
    }
}