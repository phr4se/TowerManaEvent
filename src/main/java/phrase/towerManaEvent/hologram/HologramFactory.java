package phrase.towerManaEvent.hologram;

import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.hologram.impl.DecentHologramsProvider;

public class HologramFactory {
    public static HologramProvider getHologramProvider(HologramType hologramType, TowerManaEvent plugin) {
        return switch (hologramType) {
            case DECENT_HOLOGRAMS -> new DecentHologramsProvider(plugin);
            default -> null;
        };
    }
}
