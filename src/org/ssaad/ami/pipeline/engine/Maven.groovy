package org.ssaad.ami.pipeline.engine

import lombok.Getter
import lombok.Setter

@Getter
@Setter
class Maven extends Engine {

    String settingsFile
    String options
    String goals
    String params

    @Override
    void init() {

    }

    @Override
    void execute() {

    }
}
