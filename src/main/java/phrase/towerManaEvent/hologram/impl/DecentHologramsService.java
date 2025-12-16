package phrase.towerManaEvent.hologram.impl;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.event.Chest;
import phrase.towerManaEvent.event.EventManager;
import phrase.towerManaEvent.hologram.HologramService;

import java.util.List;
import java.util.stream.Collectors;

class DecentHologramsService implements HologramService {

    private final Plugin plugin;
    private Hologram hologram;
    private List<String> lines;
    private boolean updateHologram;

    public DecentHologramsService(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createHologram(Location location, List<String> lines) {

        this.lines = lines;

        Hologram hologram = DHAPI.getHologram("TowerManaEvent");
        if(hologram != null) hologram.delete();

        this.hologram = DHAPI.createHologram("TowerManaEvent", location, getReplacedLines(lines));

        this.updateHologram = true;
        updateHologram();

    }

    @Override
    public void removeHologram() {

        this.updateHologram = false;
        hologram.delete();

    }

    private String replacePlaceholder(String message) {
        EventManager eventManager = plugin.getEventManager();
        Chest chest = eventManager.getChest();
        return message.replace("%mana%", String.valueOf(chest.getMana()))
                .replace("%stage%", String.valueOf(eventManager.getStage().getId()));
    }

    private List<String> getReplacedLines(List<String> lines) {
        return lines.stream().map(this::replacePlaceholder).collect(Collectors.toList());
    }

    private void updateHologram() {

        new BukkitRunnable() {
            @Override
            public void run() {

                if(!updateHologram) {
                    cancel();
                    return;
                }

                DHAPI.setHologramLines(hologram, getReplacedLines(lines));

            }
        }.runTaskTimer(plugin, 0L, 1L);


    }

}
