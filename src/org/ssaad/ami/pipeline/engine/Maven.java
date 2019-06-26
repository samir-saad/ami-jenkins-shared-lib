package org.ssaad.ami.pipeline.engine;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Maven extends Engine {

    private String settingsFile;
    private String options;
    private String goals;
    private String params;

    @Builder
    public Maven(String id, String name, String configDir, String credentials, String settingsFile, String options, String goals, String params) {
        super(id, name, configDir, credentials);
        this.settingsFile = settingsFile;
        this.options = options;
        this.goals = goals;
        this.params = params;
    }

    @Override
    public void init() {

    }

    @Override
    public void execute() {

    }
}
