package org.ssaad.ami.pipeline.common

import org.ssaad.ami.pipeline.stage.Stage

class Pipeline implements Serializable, Initializable, Executable {

    String id
    String name
    Application app
    //GitRepository defaultConfigRepo
    // to override the default one
    //GitRepository customConfigRepo

    // Stages
    SortedMap<String, Stage> stages

    @Override
    void init(Map config) {
        this.id = config?.id
        this.name = config?.name

    }

    @Override
    void execute(steps) {

    }
}
