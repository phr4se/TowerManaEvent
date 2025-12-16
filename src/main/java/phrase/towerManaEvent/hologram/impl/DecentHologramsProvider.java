package phrase.towerManaEvent.hologram.impl;

import org.bukkit.Location;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.hologram.HologramProvider;

import java.util.List;

public class DecentHologramsProvider extends HologramProvider {

    public DecentHologramsProvider(Plugin plugin) {
        super(new DecentHologramsService(plugin));
    }

    @Override
    public void createHologram(Location location, List<String> lines) {
        hologramService.createHologram(location, lines);
    }

    @Override
    public void removeHologram() {
        hologramService.removeHologram();
    }

}
