package phrase.towerManaEvent.config.data;

import java.util.List;

public record Other(List<String> actionsStartEvent, List<String> actionsEndEvent, List<String> actionsSwitchStage, List<String> hologramLines, String barMessage, List<String> actionsPreStageEvent, String playerActivatedMainStage) {
}
