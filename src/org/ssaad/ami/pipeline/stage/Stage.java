package org.ssaad.ami.pipeline.stage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ssaad.ami.pipeline.Executable;
import org.ssaad.ami.pipeline.Initializable;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public abstract class Stage implements Serializable, Initializable, Executable {

    private String id;
    private String name;
    private boolean enable;
    private boolean requiresConfirmation;

    public abstract boolean isActive();
}
