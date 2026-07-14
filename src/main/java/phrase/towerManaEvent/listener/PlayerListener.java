package phrase.towerManaEvent.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.api.ClickMenuChancesEvent;
import phrase.towerManaEvent.api.CloseMenuChancesEvent;
import phrase.towerManaEvent.gui.impl.MenuChancesService;
import phrase.towerManaEvent.event.Loot;
import phrase.towerManaEvent.event.EventManager;

public class PlayerListener implements Listener {
    private final TowerManaEvent plugin;

    public PlayerListener(TowerManaEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        if (event.getWhoClicked() instanceof Player player) {
            Inventory inventory = event.getClickedInventory();
            if (inventory == null || inventory.getHolder() == null) return;
            if (inventory.getHolder() instanceof MenuChancesService)
                pluginManager.callEvent(new ClickMenuChancesEvent(player, event));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        EventManager eventManager = plugin.getEventManager();
        if (!eventManager.isEventRunning()) return;
        for(Block block : event.blockList()) {
            Loot chest = eventManager.getLoot(block.getLocation());
            if (chest != null) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        Inventory inventory = event.getPlayer().getOpenInventory().getTopInventory();
        if (inventory.getHolder() instanceof MenuChancesService)
            pluginManager.callEvent(new CloseMenuChancesEvent(inventory));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        EventManager eventManager = plugin.getEventManager();
        if (!eventManager.isEventRunning()) return;
        Loot chest = eventManager.getLoot(event.getBlock().getLocation());
        if (chest != null) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof SkeletonHorse skeletonHorse) {
            if (skeletonHorse.getPersistentDataContainer().has(NamespacedKey.fromString("towermanaevent_skeleton_horse"), PersistentDataType.STRING))
                event.getDrops().clear();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        EventManager eventManager = plugin.getEventManager();
        if (!eventManager.isEventRunning()) return;
        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        Loot chest = eventManager.getLoot(block.getLocation());
        if (chest != null) {
            if (!eventManager.getStage().isOpenChest()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Fireball fireball) {
            if (fireball.getPersistentDataContainer().has(NamespacedKey.fromString("towermanaevent_fireball"), PersistentDataType.DOUBLE)) {
                double damage = fireball.getPersistentDataContainer().get(NamespacedKey.fromString("towermanaevent_fireball"), PersistentDataType.DOUBLE);
                event.setDamage(damage);
            }
        }
        if (event.getDamager() instanceof Player && event.getEntity() instanceof SkeletonHorse skeletonHorse) {
            if (skeletonHorse.getPersistentDataContainer().has(NamespacedKey.fromString("towermanaevent_skeleton_horse"), PersistentDataType.STRING))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        EventManager eventManager = plugin.getEventManager();
        if (!eventManager.isEventRunning()) return;
        if (eventManager.playerAtEvent(player))
            eventManager.getRandomLoot().addMana(plugin.getConfigFile().getSettings().plusMana());
    }
}
