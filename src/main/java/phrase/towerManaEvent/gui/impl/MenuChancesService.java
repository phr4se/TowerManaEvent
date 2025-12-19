package phrase.towerManaEvent.gui.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.gui.MenuService;
import phrase.towerManaEvent.gui.MenuType;
import phrase.towerManaEvent.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuChancesService implements MenuService, InventoryHolder {

    private final Plugin plugin;
    private final Inventory inventory;

    public MenuChancesService(Plugin plugin) {
        this.plugin = plugin;
        FileConfiguration fileConfiguration = plugin.getConfigFile().getFile("menus/menu-chances.yml");
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("menu");
        this.inventory = Bukkit.createInventory(this, configurationSection.getInt("size"), Utils.COLORIZER.colorize(configurationSection.getString("title")));
    }

    @Override
    public @NotNull Inventory getInventory() {

        ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("chances");

        configurationSection.getKeys(false).forEach(key -> {
            Material material = Material.valueOf(configurationSection.getString(key + ".material"));
            double chance = configurationSection.getDouble(key + ".chance");
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            ItemStack itemStack = new ItemStack(material);
            if(configurationSection.contains(key + ".enchantments")) {
                configurationSection.getStringList(key + ".enchantments").forEach(string -> {
                    String[] strings = string.split(";");
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(strings[0]));
                    int level = Integer.parseInt(strings[1]);
                    enchantments.put(enchantment, level);
                });
                itemStack.addEnchantments(enchantments);
            }
            itemStack.getItemMeta().getPersistentDataContainer().set(NamespacedKey.fromString("chance"), PersistentDataType.DOUBLE, chance);
            itemStack.setLore(List.of(String.valueOf(chance)));
            inventory.addItem(itemStack);
        });

        return inventory;
    }

    @Override
    public Inventory create(Player player) {
        return inventory;
    }

}
