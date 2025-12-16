package phrase.towerManaEvent.hologram;

import org.bukkit.Location;

import java.util.List;

public abstract class HologramProvider {

    protected final HologramService hologramService;

    public HologramProvider(HologramService hologramService) {
        this.hologramService = hologramService;
    }

    public abstract void createHologram(Location location, List<String> lines);
    public abstract void removeHologram();

}
