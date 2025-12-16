package phrase.towerManaEvent.hologram;

import org.bukkit.Location;

import java.util.List;

public interface HologramService {

    void createHologram(Location location, List<String> lines);
    void removeHologram();

}
