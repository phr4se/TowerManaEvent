package phrase.towerManaEvent.hologram.impl;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.event.EventManager;
import phrase.towerManaEvent.hologram.HologramService;
import phrase.towerManaEvent.event.Loot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class DecentHologramsService implements HologramService {

    private final TowerManaEvent plugin;
    private final Map<Loot, Hologram> holograms = new HashMap<>();
    private final Map<Loot, List<String>> lines = new HashMap<>();
    private boolean updateHologram;

    public DecentHologramsService(TowerManaEvent plugin) {
        this.plugin = plugin;
    }

    @Override
    public void createHologram(Location location, Loot loot, List<String> lines) {

        this.lines.put(loot, lines);

        Hologram hologram = DHAPI.getHologram(loot.getUuid().toString());
        if(hologram != null) hologram.delete();

        holograms.put(loot, DHAPI.createHologram(loot.getUuid().toString(), location, getReplacedLines(loot)));

        this.updateHologram = true;
        updateHologram();

    }

    @Override
    public void removeHologram(Loot loot) {
        this.updateHologram = false;
        holograms.remove(loot).delete();
        lines.remove(loot);
    }

    private String replacePlaceholder(Loot loot, String message) {
        EventManager eventManager = plugin.getEventManager();
        return message.replace("%mana%", String.valueOf(loot.getMana()))
                .replace("%stage%", String.valueOf(eventManager.getStage().getId()))
                .replace("%ability%", (eventManager.getStage().getLatestUsedAbility() != null) ? eventManager.getStage().getLatestUsedAbility().getName() : "-")
                .replace("%status%", (eventManager.getStage().isOpenChest()) ? "открыт" : "закрыт");
    }

    private List<String> getReplacedLines(Loot loot) {
        return lines.get(loot).stream().map(string -> replacePlaceholder(loot, string)).collect(Collectors.toList());
    }

    private void updateHologram() {

        new BukkitRunnable() {
            @Override
            public void run() {

                if(!updateHologram) {
                    cancel();
                    return;
                }

                holograms.forEach((key, value) -> DHAPI.setHologramLines(value, getReplacedLines(key)));

            }
        }.runTaskTimer(plugin, 0L, 1L);


    }

}
