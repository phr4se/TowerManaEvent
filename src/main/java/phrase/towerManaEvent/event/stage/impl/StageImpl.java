package phrase.towerManaEvent.event.stage.impl;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.event.ability.AbilityType;
import phrase.towerManaEvent.event.EventManager;
import phrase.towerManaEvent.event.stage.Stage;
import phrase.towerManaEvent.util.MaskedRealType;

import java.util.List;
import java.util.Random;

public class StageImpl extends Stage {
    private final List<AbilityType> availableAbilities;

    public StageImpl(int id, boolean pvp, int duration, TowerManaEvent plugin, List<AbilityType> availableAbilities, boolean openChest, boolean airOrLightingDrop, boolean withLighting) {
        this(id, pvp, duration, plugin, availableAbilities, openChest, airOrLightingDrop, withLighting, false);
    }

    public StageImpl(int id, boolean pvp, int duration, TowerManaEvent plugin, List<AbilityType> availableAbilities, boolean openChest, boolean airOrLightingDrop, boolean withLighting, boolean included) {
        super(id, pvp, duration, plugin, openChest, airOrLightingDrop, withLighting, included);
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
                        if(key.getBlock().getState() instanceof Chest chest) {
                            Inventory inventory = value.getInventory();
                            Random random = new Random();
                            ItemStack[] contents = plugin.getLootManager().getRandomLoots(101);
                            for (ItemStack itemStack : contents) {
                                int randomSlot = random.nextInt(inventory.getSize());
                                value.getMaskedRealType().mask(randomSlot, itemStack.getType());
                                itemStack.setType(MaskedRealType.MASK.stream().toList().get(new Random().nextInt(MaskedRealType.MASK.size())));
                                inventory.setItem(randomSlot, itemStack);
                            }
                            chest.getInventory().setContents(inventory.getContents());
                        }
                    });
                }
            }.runTask(plugin);
        }
        if(isAirOrLightingDrop()) eventManager.startTaskAirOrLightingDrop(isWithLighting());
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
                    if(isIncluded()) {
                        plugin.getEventManager().stopEvent();
                        return;
                    }
                    plugin.getEventManager().switchStage();
                } else {
                    if (remained > 0) remained--;
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }
}
