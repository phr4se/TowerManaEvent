package phrase.towerManaEvent.hologram;

import org.bukkit.Location;
import phrase.towerManaEvent.event.Loot;

import java.util.List;

public interface HologramService {

    void createHologram(Location location, Loot loot, List<String> lines);
    void removeHologram(Loot loot);

}
