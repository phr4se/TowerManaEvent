package phrase.towerManaEvent.hologram;

import org.bukkit.Location;
import phrase.towerManaEvent.event.Loot;

import java.util.List;

public abstract class HologramProvider {

    protected final HologramService hologramService;

    public HologramProvider(HologramService hologramService) {
        this.hologramService = hologramService;
    }

    public abstract void createHologram(Location location, Loot loot, List<String> lines);
    public abstract void removeHologram(Loot loot);

}
