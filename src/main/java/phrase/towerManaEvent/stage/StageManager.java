package phrase.towerManaEvent.stage;

import phrase.towerManaEvent.Plugin;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

    private final Plugin plugin;
    private final Map<Integer, Stage> stages;

    public StageManager(Plugin plugin) {
        this.plugin = plugin;
        this.stages = new HashMap<>();
        initialize();
    }

    private void initialize() {

        for(StageType stageType : StageType.values()) {
            stageType.initialize(plugin);
            Stage stage = stageType.getStage();
            stages.put(stage.getId() - 1, stage);
        }

    }

    public Stage getNextStage(Stage previousStage) {
        return stages.get(previousStage.getId() + 1);
    }

    public Map<Integer, Stage> getStages() {
        return stages;
    }

}
