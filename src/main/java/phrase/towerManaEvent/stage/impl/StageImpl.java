package phrase.towerManaEvent.stage.impl;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.AbilityType;
import phrase.towerManaEvent.stage.Stage;

import java.util.List;

public class StageImpl extends Stage {

    private final List<AbilityType> availableAbilities;

    public StageImpl(int id, boolean pvp, int duration, Plugin plugin, List<AbilityType> availableAbilities, boolean openChest) {
        super(id, pvp, duration, plugin, openChest);
        this.availableAbilities = availableAbilities;
    }

    @Override
    public void setup() {
        startTask();
        if(openChest) {
            Inventory inventory = ((Chest)plugin.getEventManager().getChest().getLocation().getBlock().getState()).getInventory();
            for(ItemStack itemStack : plugin.getLootManager().getRandomLoots()) inventory.addItem(itemStack);
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

                if(remained == 0) {
                    cancel();
                    plugin.getEventManager().switchStage();
                } else {
                    if(remained > 0) remained--;
                }

            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);

    }

}
