package phrase.towerManaEvent.hologram.impl;

import org.bukkit.Location;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.hologram.HologramProvider;
import phrase.towerManaEvent.event.Loot;

import java.util.List;

public class DecentHologramsProvider extends HologramProvider {

    public DecentHologramsProvider(TowerManaEvent plugin) {
        super(new DecentHologramsService(plugin));
    }

    @Override
    public void createHologram(Location location, Loot loot, List<String> lines) {
        hologramService.createHologram(location, loot, lines);
    }

    @Override
    public void removeHologram(Loot loot) {
        hologramService.removeHologram(loot);
    }

}
