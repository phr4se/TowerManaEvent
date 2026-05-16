package phrase.towerManaEvent.config.data;

import java.util.List;

public record CommandMessages(String eventAlreadyRun, String schematicDamaged, String schematicNotExist,
                              String eventRun, List<String> manual) {
}
