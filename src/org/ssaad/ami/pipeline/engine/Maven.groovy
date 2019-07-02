package org.ssaad.ami.pipeline.engine

class Maven extends Engine {

    String settingsFile
    String options
    String goals
    String params

    @Override
    void init(Map config) {

    }

    @Override
    void execute(steps) {
        if (steps != null) {
            steps.sh "mvn ${this.goals} ${this.params}"
        }
    }
}
