package phrase.towerManaEvent.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.api.ClickMenuChancesEvent;
import phrase.towerManaEvent.api.CloseMenuChancesEvent;
import phrase.towerManaEvent.event.LootManager;
import phrase.towerManaEvent.gui.MenuType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ManaListener implements Listener {
    private final TowerManaEvent plugin;

    public ManaListener(TowerManaEvent plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClickMenuChances(ClickMenuChancesEvent event) {
        if (event.getCurrentItem() == null) return;
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        LootManager lootManager = plugin.getLootManager();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String key;
        if (persistentDataContainer.has(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING))
            key = persistentDataContainer.get(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING);
        else {
            key = UUID.randomUUID().toString();
            persistentDataContainer.set(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING, key);
            itemStack.setItemMeta(itemMeta);
            lootManager.add(key, itemStack, 0.0);
        }
        double chance = lootManager.getChance(key);
        if (event.isLeftClick()) chance += 10;
        else chance -= 10;
        lootManager.recordChance(key, chance);
        plugin.getMenuManager().showMenu(event.getPlayer(), MenuType.MENU_CHANCES);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCloseMenuChances(CloseMenuChancesEvent event) throws IOException {
        LootManager lootManager = plugin.getLootManager();
        final FileConfiguration fileConfiguration = plugin.getConfigFile().getFile("chances.yml");
        final ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("chances");
        for (ItemStack itemStack : event.getInventory().getContents()) {
            if (itemStack == null) continue;
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            String key;
            if (persistentDataContainer.has(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING))
                key = persistentDataContainer.get(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING);
            else {
                key = UUID.randomUUID().toString();
                persistentDataContainer.set(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING, key);
                itemStack.setItemMeta(itemMeta);
                lootManager.add(key, itemStack, 0.0);
            }
            double chance = lootManager.getChance(key);
            configurationSection.set(key + ".material", itemStack.getType().toString());
            configurationSection.set(key + ".chance", String.valueOf(chance));
            configurationSection.set(key + ".amount", String.valueOf(itemStack.getAmount()));
            configurationSection.set(key + ".display-name", itemMeta.getDisplayName());
            configurationSection.set(key + ".lore", lootManager.getItems().get(key).getLore());
            List<String> enchants = new ArrayList<>();
            if (itemMeta.hasEnchants()) {
                for (Map.Entry<Enchantment, Integer> entry : itemMeta.getEnchants().entrySet())
                    enchants.add(entry.getKey().getKey() + ";" + entry.getValue());
                configurationSection.set(key + ".enchantments", enchants);
            }
            fileConfiguration.save(new File(plugin.getDataFolder() + "/" + plugin.getConfigFile().getLanguage().name, "chances.yml"));
        }
    }
}
