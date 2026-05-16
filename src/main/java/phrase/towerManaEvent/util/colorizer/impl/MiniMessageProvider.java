package phrase.towerManaEvent.util.colorizer.impl;

import phrase.towerManaEvent.util.colorizer.ColorizerProvider;

public class MiniMessageProvider extends ColorizerProvider {
    public MiniMessageProvider() {
        super(new MiniMessageService());
    }

    @Override
    public String colorize(String message) {
        return colorizerService.colorize(message);
    }
}
