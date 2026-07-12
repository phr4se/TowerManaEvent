package phrase.towerManaEvent.event.stage;

import phrase.towerManaEvent.TowerManaEvent;

import java.util.HashMap;
import java.util.Map;

public class StageManager {
    private final TowerManaEvent plugin;
    private final Map<Integer, Stage> stages;
    private int current = 0;

    public StageManager(TowerManaEvent plugin) {
        this.plugin = plugin;
        this.stages = new HashMap<>();
        initialize();
    }

    private void initialize() {
        int i = 0;
        for (StageType stageType : StageType.values()) {
            stageType.initialize(plugin);
            Stage stage = stageType.getStage();
            stages.put(i++, stage);
        }
    }

    public Stage getFirstStage() {
        Stage stage = stages.get(current);
        stage.setRemained(stage.getDuration());
        return stage;
    }

    public Stage getNextStage() {
        Stage stage = stages.get(++current);
        if (stage == null) current = 0;
        else stage.setRemained(stage.getDuration());
        return stage;
    }
}
