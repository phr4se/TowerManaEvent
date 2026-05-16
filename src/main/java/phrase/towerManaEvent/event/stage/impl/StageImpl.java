package phrase.towerManaEvent.event.stage.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.event.ability.AbilityType;
import phrase.towerManaEvent.event.EventManager;
import phrase.towerManaEvent.event.stage.Stage;

import java.util.List;
import java.util.Random;

public class StageImpl extends Stage {
    private final List<AbilityType> availableAbilities;

    public StageImpl(int id, boolean pvp, int duration, Plugin plugin, List<AbilityType> availableAbilities, boolean openChest) {
        super(id, pvp, duration, plugin, openChest);
        this.availableAbilities = availableAbilities;
    }

    @Override
    public void setup() {
        startTask();
        EventManager eventManager = plugin.getEventManager();
        eventManager.setPvp(pvp);
        if (openChest) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    eventManager.getLoots().forEach((key, value) -> {
                        Inventory inventory = value.getInventory();
                        Random random = new Random();
                        ItemStack[] contents = plugin.getLootManager().getRandomLoots(101);
                        for (ItemStack itemStack : contents) {
                            int randomSlot = random.nextInt(inventory.getSize());
                            inventory.setItem(randomSlot, itemStack);
                            ;
                        }
                    });
                }
            }.runTask(plugin);
        }
    }

    @Override
    public List<AbilityType> getAvailableAbilities() {
        return availableAbilities;
    }

    private void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (remained == 0) {
                    cancel();
                    plugin.getEventManager().switchStage();
                } else {
                    if (remained > 0) remained--;
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
}
