package phrase.towerManaEvent.action.context.impl;

import phrase.towerManaEvent.action.context.Context;
import phrase.towerManaEvent.util.Utils;

public record StringContext(String message) implements Context {
    public static StringContext validate(String message) {
        return new StringContext(Utils.colorizer.colorize(message));
    }
}
