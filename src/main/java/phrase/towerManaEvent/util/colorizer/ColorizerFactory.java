package phrase.towerManaEvent.util.colorizer;

import phrase.towerManaEvent.util.colorizer.impl.HexProvider;

public class ColorizerFactory {

    public static ColorizerProvider getProvider(ColorizerType colorizerType) {
        return switch (colorizerType) {
            case HEX -> new HexProvider();
            default -> null;
        };
    }

}
