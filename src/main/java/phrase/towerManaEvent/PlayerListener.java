package phrase.towerManaEvent;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import phrase.towerManaEvent.event.Chest;
import phrase.towerManaEvent.event.EventManager;

public class PlayerListener implements Listener {

    private final Plugin plugin;

    public PlayerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        EventManager eventManager = plugin.getEventManager();

        if(!eventManager.isEventRunning()) return;

        Chest chest = eventManager.getChest();

        Block block = event.getBlock();

        if(chest.getLocation().equals(block.getLocation())) event.setCancelled(true);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        EventManager eventManager = plugin.getEventManager();

        if(!eventManager.isEventRunning()) return;

        Chest chest = eventManager.getChest();

        if(event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();

        if (chest.getLocation().equals(block.getLocation())) {

            if(!eventManager.getStage().isOpenChest()) event.setCancelled(true);

        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getPlayer();

        EventManager eventManager = plugin.getEventManager();

        if(!eventManager.isEventRunning()) return;

        if(eventManager.playerAtEvent(player)) eventManager.getChest().addMana(plugin.getConfigFile().getSettings().plusMana());

    }

}
