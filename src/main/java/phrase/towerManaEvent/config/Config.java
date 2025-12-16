package phrase.towerManaEvent.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.Ability;
import phrase.towerManaEvent.ability.AbilityType;
import phrase.towerManaEvent.ability.impl.SplashPunch;
import phrase.towerManaEvent.config.data.*;
import phrase.towerManaEvent.hologram.HologramType;
import phrase.towerManaEvent.util.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {

    private final Plugin plugin;
    private String prefix;
    private Messages messages;
    private CommandMessages commandMessages;
    private Settings settings;
    private FireballSettings fireballSettings;
    private HorseSettings horseSettings;
    private SpiderWebSettings spiderWebSettings;
    private SplashPunchSettings splashPunchSettings;


    public Config(Plugin plugin) {
        this.plugin = plugin;

        makeFolders();
    }

    private void makeFolders() {

        File schematicsFolder = new File(plugin.getDataFolder() + "/schematics");
        if(!schematicsFolder.exists()) schematicsFolder.mkdirs();

    }

    public void createFiles(String... filesName) {

        for (String fileName : filesName) {

            File file = new File(plugin.getDataFolder(), fileName);

            if(!file.exists()) {
                plugin.saveResource(file.getPath(), false);
            }

        }

    }

    public FileConfiguration getFile(String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    public void setupMessages() {

        final ConfigurationSection configurationSectionMessages = plugin.getConfig().getConfigurationSection("messages");

        prefix = Utils.COLORIZER.colorize(configurationSectionMessages.getString("prefix"));

        messages = new Messages();

        final ConfigurationSection configurationSectionCommandMessages = configurationSectionMessages.getConfigurationSection("command");

        commandMessages = new CommandMessages();

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

        final Map<ItemStack, Double> chances = new HashMap<>();

        final ConfigurationSection configurationSectionChances = configurationSection.getConfigurationSection("chances");
        configurationSectionChances.getKeys(false).forEach(key -> {
            Material material = Material.valueOf(configurationSectionChances.getString(key + ".material"));
            double chance = configurationSectionChances.getDouble(key + ".chance");
            Map<Enchantment, Integer> enchantments = new HashMap<>();
            configurationSectionChances.getStringList(key + ".enchantments").forEach(string -> {
                String[] strings = string.split(";");
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(strings[0]));
                int level = Integer.parseInt(strings[1]);
                enchantments.put(enchantment, level);
            });
            ItemStack itemStack = new ItemStack(material);
            itemStack.addEnchantments(enchantments);
            chances.put(itemStack, chance);
        });

        settings = new Settings(HologramType.valueOf(configurationSection.getString("hologram-provider").toUpperCase()),
                Bukkit.getWorld(configurationSection.getString("world-name")),
                configurationSection.getInt("coordinate-range-x"),
                configurationSection.getInt("coordinate-range-z"),
                configurationSection.getString("schematic-name"),
                configurationSection.getLong("use-abilities"),
                abilities,
                configurationSection.getInt("mana"),
                configurationSection.getStringList("hologram-lines"),
                chances,
                configurationSection.getStringList("messages-start-event"),
                configurationSection.getStringList("messages-end-event"),
                configurationSection.getString("bar-message"),
                BarColor.valueOf(configurationSection.getString("bar-color")),
                BarStyle.valueOf(configurationSection.getString("bar-style")),
                configurationSection.getStringList("bar-flags").stream().map(BarFlag::valueOf).toArray(BarFlag[]::new));

    }

    public void setupAbilitiesSettings() {

        final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("settings.settings-abilities");

        final ConfigurationSection configurationSectionFireball = configurationSection.getConfigurationSection("fireball");

        fireballSettings = new FireballSettings(configurationSectionFireball.getInt("radius-serach-players"),
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
                configurationSectionSplashPunch.getInt("step"),
                configurationSectionSplashPunch.getInt("radius-search-players"),
                configurationSectionSplashPunch.getLong("later-back"),
                configurationSectionSplashPunch.getLong("later-back-particle")
        );

    }

    public String getMessagePrefixed(String message) {
        if(message == null || prefix == null) {
            return message;
        }
        return Utils.COLORIZER.colorize(message.replace("%prefix%", prefix));
    }

    public List<String> getMessagesPrefixed(List<String> messages) {
        if(messages == null || messages.isEmpty()) {
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

}
