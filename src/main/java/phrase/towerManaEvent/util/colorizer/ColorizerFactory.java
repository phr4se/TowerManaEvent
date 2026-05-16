package phrase.towerManaEvent.util.colorizer;

import phrase.towerManaEvent.util.colorizer.impl.HexProvider;
import phrase.towerManaEvent.util.colorizer.impl.MiniMessageProvider;

public class ColorizerFactory {
    public static ColorizerProvider getProvider(ColorizerType colorizerType) {
        return switch (colorizerType) {
            case HEX -> new HexProvider();
            case MINI_MESSAGE -> new MiniMessageProvider();
            default -> null;
        };
    }
}
