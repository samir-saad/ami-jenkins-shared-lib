package org.ssaad.ami.pipeline.stage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.ssaad.ami.pipeline.engine.Engine;

@Getter
@Setter
public class EngineStage extends Stage {

    private Engine engine;

    @Builder
    public EngineStage(String id, String name, boolean enable, boolean requiresConfirmation, Engine engine) {
        super(id, name, enable, requiresConfirmation);
        this.engine = engine;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void init() {
        engine.init();
    }

    @Override
    public void execute() {
        engine.execute();
    }
}
