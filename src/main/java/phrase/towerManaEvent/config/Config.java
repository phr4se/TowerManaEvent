package phrase.towerManaEvent.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.event.ability.AbilityType;
import phrase.towerManaEvent.config.data.*;
import phrase.towerManaEvent.hologram.HologramType;
import phrase.towerManaEvent.util.Utils;
import phrase.towerManaEvent.util.colorizer.ColorizerType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    private final TowerManaEvent plugin;
    private String prefix;
    private Messages messages;
    private CommandMessages commandMessages;
    private Settings settings;
    private FireballSettings fireballSettings;
    private HorseSettings horseSettings;
    private SpiderWebSettings spiderWebSettings;
    private SplashPunchSettings splashPunchSettings;
    private Other other;

    public enum Language {
        RU("ru"),
        EN("en");
        public final String name;

        Language(String name) {
            this.name = name;
        }
    }

    private Language language;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(FileConfiguration fileConfiguration) {
        language = Language.valueOf(fileConfiguration.getString("language").toUpperCase());
    }

    public Config(TowerManaEvent plugin) {
        this.plugin = plugin;
        makeFolders();
    }

    private void makeFolders() {
        File schematicsFolder = new File(plugin.getDataFolder() + "/schematics");
        if (!schematicsFolder.exists()) schematicsFolder.mkdirs();
    }

    public FileConfiguration getDefaultFile(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) plugin.saveResource(fileName, false);
        return YamlConfiguration.loadConfiguration(file);
    }

    public void createFiles(String... filesName) {
        for (String fileName : filesName) {
            File file = new File(plugin.getDataFolder() + language.name, fileName);
            if (!file.exists()) {
                plugin.saveResource(language.name + "/" + fileName, false);
            }
        }
    }

    public FileConfiguration getFile(String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + "/" + language.name, fileName));
    }

    public void setupMessages() {
        final ConfigurationSection configurationSectionMessages = getFile("messages.yml").getConfigurationSection("messages");
        prefix = Utils.colorizer.colorize(configurationSectionMessages.getString("prefix"));
        messages = new Messages(getMessagePrefixed(configurationSectionMessages.getString("no-permission")),
                getMessagePrefixed(configurationSectionMessages.getString("unknown-command")),
                getMessagePrefixed(configurationSectionMessages.getString("error")),
                getMessagePrefixed(configurationSectionMessages.getString("incorrect-arguments")),
                getMessagePrefixed(configurationSectionMessages.getString("not-a-player")));
        final ConfigurationSection configurationSectionCommandMessages = configurationSectionMessages.getConfigurationSection("command");
        commandMessages = new CommandMessages(getMessagePrefixed(configurationSectionCommandMessages.getString("event-already-run")),
                getMessagePrefixed(configurationSectionCommandMessages.getString("schematic-damaged")),
                getMessagePrefixed(configurationSectionCommandMessages.getString("schematic-not-exist")),
                getMessagePrefixed(configurationSectionCommandMessages.getString("event-run")),
                getMessagesPrefixed(configurationSectionCommandMessages.getStringList("manual")));
    }

    public void setupSettings() {
        final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("settings");
        final Map<AbilityType, Integer> abilities = new HashMap<>();
        configurationSection.getStringList("abilities").forEach(string -> {
            String[] strings = string.split(";");
            AbilityType abilityType = AbilityType.valueOf(strings[0]);
            int mana = Integer.parseInt(strings[1]);
            abilities.put(abilityType, mana);
        });
        final Map<String, Double> chances = new HashMap<>();
        final Map<String, ItemStack> items = new HashMap<>();
        final FileConfiguration fileConfiguration = getFile("chances.yml");
        final ConfigurationSection configurationSectionChances = fileConfiguration.getConfigurationSection("chances");
        configurationSectionChances.getKeys(false).forEach(key -> {
            Material material = Material.valueOf(configurationSectionChances.getString(key + ".material"));
            double chance = Double.parseDouble(configurationSectionChances.getString(key + ".chance"));
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            ItemStack itemStack = new ItemStack(material);
            if (configurationSectionChances.contains(key + ".enchantments")) {
                configurationSectionChances.getStringList(key + ".enchantments").forEach(string -> {
                    String[] strings = string.split(";");
                    Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(strings[0]));
                    int level = Integer.parseInt(strings[1]);
                    enchantments.put(enchantment, level);
                });
                itemStack.addEnchantments(enchantments);
            }
            int amount = Integer.parseInt(configurationSectionChances.getString(key + ".amount"));
            String displayName = configurationSection.getString(key + ".display-name");
            List<String> lore;
            if (configurationSection.contains(key + ".lore")) lore = configurationSection.getStringList(key + ".lore");
            else lore = new ArrayList<>();
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            persistentDataContainer.set(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING, key);
            itemMeta.setDisplayName(displayName);
            itemStack.setItemMeta(itemMeta);
            itemStack.setAmount(amount);
            itemStack.setLore(lore);
            chances.put(key, chance);
            items.put(key, itemStack);
        });
        settings = new Settings(ColorizerType.valueOf(configurationSection.getString("colorizer-type").toUpperCase()), HologramType.valueOf(configurationSection.getString("hologram-provider").toUpperCase()),
                Bukkit.getWorld(configurationSection.getString("world-name")),
                configurationSection.getInt("coordinate-range-x"),
                configurationSection.getInt("coordinate-range-z"),
                configurationSection.getString("schematic-name"),
                configurationSection.getLong("use-abilities"),
                abilities,
                configurationSection.getInt("mana"),
                chances,
                BarColor.valueOf(configurationSection.getString("bar-color")),
                BarStyle.valueOf(configurationSection.getString("bar-style")),
                (configurationSection.contains("bar-flags")) ? configurationSection.getStringList("bar-flags").stream().map(BarFlag::valueOf).toArray(BarFlag[]::new) : new BarFlag[0],
                configurationSection.getInt("plus-mana"),
                configurationSection.getString("type"),
                configurationSection.getStringList("region-flags"),
                configurationSection.getInt("plus-mana-stage"),
                items);
    }

    public void setupOther() {
        final ConfigurationSection configurationSection = getFile("other.yml").getConfigurationSection("other");
        other = new Other(configurationSection.getStringList("actions-start-event"),
                configurationSection.getStringList("actions-end-event"),
                configurationSection.getStringList("actions-switch-stage"),
                configurationSection.getStringList("hologram-lines"),
                configurationSection.getString("bar-message"));
    }

    public void setupAbilitiesSettings() {
        final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("settings.settings-abilities");
        final ConfigurationSection configurationSectionFireball = configurationSection.getConfigurationSection("fireball");
        fireballSettings = new FireballSettings(configurationSectionFireball.getInt("radius-search-players"),
                configurationSectionFireball.getDouble("damage"),
                configurationSectionFireball.getInt("count-fireball"),
                configurationSectionFireball.getInt("boost-y"),
                configurationSectionFireball.getInt("offset-location-x"),
                configurationSectionFireball.getInt("offset-location-z"),
                configurationSectionFireball.getInt("offset-x"),
                configurationSectionFireball.getInt("offset-y"),
                configurationSectionFireball.getInt("offset-z"),
                configurationSectionFireball.getInt("speed"));
        final ConfigurationSection configurationSectionHorse = configurationSection.getConfigurationSection("horse");
        horseSettings = new HorseSettings(
                configurationSectionHorse.getDouble("damage"),
                configurationSectionHorse.getInt("distance"),
                configurationSectionHorse.getInt("num-one"),
                configurationSectionHorse.getInt("num-two"),
                configurationSectionHorse.getInt("forward-blocks"),
                configurationSectionHorse.getDouble("speed"),
                configurationSectionHorse.getInt("knockback-blocks"),
                configurationSectionHorse.getLong("later-death")
        );
        final ConfigurationSection configurationSectionSpiderWeb = configurationSection.getConfigurationSection("spider-web");
        spiderWebSettings = new SpiderWebSettings(
                configurationSectionSpiderWeb.getInt("radius-search-players"),
                configurationSectionSpiderWeb.getDouble("damage"),
                configurationSectionSpiderWeb.getInt("radius"),
                configurationSectionSpiderWeb.getLong("later-remove")
        );
        final ConfigurationSection configurationSectionSplashPunch = configurationSection.getConfigurationSection("splash-punch");
        splashPunchSettings = new SplashPunchSettings(
                configurationSectionSplashPunch.getDouble("damage"),
                configurationSectionSplashPunch.getInt("count"),
                configurationSectionSplashPunch.getLong("later-count"),
                configurationSectionSplashPunch.getInt("radius"),
                configurationSectionSplashPunch.getInt("step-radius"),
                configurationSectionSplashPunch.getLong("later-forward"),
                configurationSectionSplashPunch.getInt("particle-count"),
                configurationSectionSplashPunch.getLong("later-forward-particle"),
                configurationSectionSplashPunch.getDouble("step"),
                configurationSectionSplashPunch.getInt("radius-search-players"),
                configurationSectionSplashPunch.getLong("later-back"),
                configurationSectionSplashPunch.getLong("later-back-particle")
        );
    }

    public String getMessagePrefixed(String message) {
        if (message == null || prefix == null) {
            return message;
        }
        return Utils.colorizer.colorize(message.replace("%prefix%", prefix));
    }

    public List<String> getMessagesPrefixed(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return messages;
        }
        return messages.stream().map(this::getMessagePrefixed).collect(Collectors.toList());
    }

    public Messages getMessages() {
        return messages;
    }

    public CommandMessages getCommandMessages() {
        return commandMessages;
    }

    public Settings getSettings() {
        return settings;
    }

    public FireballSettings getFireballSettings() {
        return fireballSettings;
    }

    public HorseSettings getHorseSettings() {
        return horseSettings;
    }

    public SpiderWebSettings getSpiderWebSettings() {
        return spiderWebSettings;
    }

    public SplashPunchSettings getSplashPunchSettings() {
        return splashPunchSettings;
    }

    public Other getOther() {
        return other;
    }
}
