package org.ssaad.ami.pipeline;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ssaad.ami.pipeline.stage.Stage;

import java.io.Serializable;
import java.util.SortedMap;

@Getter
@Setter
public class Pipeline implements Serializable, Initializable, Executable {

    private String id;
    private String name;
    private Application app;
    //GitRepository defaultConfigRepo
    // to override the default one
    //GitRepository customConfigRepo

    // Stages
    Stage build;
    SortedMap stages;

    @Builder
    public Pipeline(String id, String name, Application app, Stage build, SortedMap stages) {
        this.id = id;
        this.name = name;
        this.app = app;
        this.build = build;
        this.stages = stages;
    }

    @Override
    public void init() {

    }

    @Override
    public void execute() {

    }
}
