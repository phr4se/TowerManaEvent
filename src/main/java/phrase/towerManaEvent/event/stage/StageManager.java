package phrase.towerManaEvent.event.stage;

import phrase.towerManaEvent.Plugin;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

    private final Plugin plugin;
    private final Map<Integer, Stage> stages;
    private int current = 0;

    public StageManager(Plugin plugin) {
        this.plugin = plugin;
        this.stages = new HashMap<>();
        initialize();
    }

    private void initialize() {

        int i = 0;
        for(StageType stageType : StageType.values()) {
            stageType.initialize(plugin);
            Stage stage = stageType.getStage();
            stages.put(i++, stage);
        }

    }

    public Stage getNextStage() {
        return stages.get(++current);
    }

    public Map<Integer, Stage> getStages() {
        return stages;
    }

}
