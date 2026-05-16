package phrase.towerManaEvent.hologram;

import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.hologram.impl.DecentHologramsProvider;

public class HologramFactory {
    public static HologramProvider getHologramProvider(HologramType hologramType, Plugin plugin) {
        return switch (hologramType) {
            case DECENT_HOLOGRAMS -> new DecentHologramsProvider(plugin);
            default -> null;
        };
    }
}
