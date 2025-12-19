package phrase.towerManaEvent.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.api.ClickMenuChancesEvent;
import phrase.towerManaEvent.api.CloseMenuChancesEvent;
import phrase.towerManaEvent.event.LootManager;
import phrase.towerManaEvent.gui.MenuType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ManaListener implements Listener {

    private final Plugin plugin;

    public ManaListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickMenuChances(ClickMenuChancesEvent event) {

        if(event.getCurrentItem() == null) return;

        ItemStack itemStack = event.getCurrentItem();
        PersistentDataContainer persistentDataContainer = itemStack.getItemMeta().getPersistentDataContainer();

        double chance = persistentDataContainer.get(NamespacedKey.fromString("chance"), PersistentDataType.DOUBLE);

        if(event.isLeftClick()) chance += 10;
        else chance -= 10;

        persistentDataContainer.set(NamespacedKey.fromString("chance"), PersistentDataType.DOUBLE, chance);

        event.getCurrentItem().setLore(List.of(String.valueOf(chance)));

        plugin.getMenuManager().showMenu(event.getPlayer(), MenuType.MENU_CHANCES);

    }

    @EventHandler
    public void onCloseMenuChances(CloseMenuChancesEvent event) {

        LootManager lootManager = plugin.getLootManager();

        for (ItemStack itemStack : event.getInventory().getContents()) {

            ItemMeta itemMeta = itemStack.getItemMeta();

            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            double chance = persistentDataContainer.get(NamespacedKey.fromString("chance"), PersistentDataType.DOUBLE);

            lootManager.recordItemStack(itemStack, chance);

            ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("settings.chances");

            String randomUUID = UUID.randomUUID().toString();

            configurationSection.set(randomUUID + ".material", itemStack.getType().toString());
            configurationSection.set(randomUUID + ".chance", String.valueOf(chance));

            List<String> enchants = new ArrayList<>();
            if(itemMeta.hasEnchants()) {

                for(Map.Entry<Enchantment, Integer> entry : itemMeta.getEnchants().entrySet()) enchants.add(entry.getKey().getKey() + ";" + entry.getValue());
                configurationSection.set(randomUUID + ".enchantments", enchants);

            }

        }

        try {
            plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
