package phrase.towerManaEvent.util.colorizer.impl;

import phrase.towerManaEvent.util.colorizer.ColorizerProvider;

public class HexProvider extends ColorizerProvider {
    public HexProvider() {
        super(new HexService());
    }

    @Override
    public String colorize(String message) {
        return colorizerService.colorize(message);
    }
}
