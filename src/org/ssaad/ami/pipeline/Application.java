package org.ssaad.ami.pipeline;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Application implements Serializable {

    private String id;
    private String group;
    private String name;
    private String description;
    private  String version;
    private  String branch;
    // service or library
    private String type;
    private String latestCommit;
}
