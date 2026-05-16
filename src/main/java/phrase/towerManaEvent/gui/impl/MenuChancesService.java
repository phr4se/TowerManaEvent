package phrase.towerManaEvent.gui.impl;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.event.LootManager;
import phrase.towerManaEvent.gui.MenuService;
import phrase.towerManaEvent.util.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class MenuChancesService implements MenuService, InventoryHolder {
    private final Plugin plugin;
    private final Inventory inventory;

    public MenuChancesService(Plugin plugin) {
        this.plugin = plugin;
        FileConfiguration fileConfiguration = plugin.getConfigFile().getFile("menus/menu-chances.yml");
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("menu");
        this.inventory = Bukkit.createInventory(this, configurationSection.getInt("size"), Utils.colorizer.colorize(configurationSection.getString("title")));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public Inventory create(Player player) {
        FileConfiguration fileConfiguration = plugin.getConfigFile().getFile("menus/menu-chances.yml");
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("menu");
        List<String> addLore = configurationSection.getStringList("add-lore").stream().map(Utils.colorizer::colorize).collect(Collectors.toList());
        LootManager lootManager = plugin.getLootManager();
        lootManager.getItems().entrySet().stream().forEach(entry -> {
            ItemStack itemStack = entry.getValue();
            List<String> lore;
            if (itemStack.getLore() == null) lore = new ArrayList<>();
            else lore = itemStack.getLore();
            double chance = lootManager.getChance(entry.getKey());
            lore.addAll(addLore.stream().map(string -> string.replace("%chance%", String.valueOf(chance))).collect(Collectors.toList()));
            ItemStack newItemStack = itemStack.clone();
            newItemStack.setLore(lore);
            inventory.addItem(newItemStack);
        });
        return inventory;
    }
}
