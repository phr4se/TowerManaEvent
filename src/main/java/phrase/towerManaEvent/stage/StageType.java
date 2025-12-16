package phrase.towerManaEvent.stage;

import org.bukkit.configuration.ConfigurationSection;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.AbilityType;
import phrase.towerManaEvent.stage.impl.StageImpl;

import java.util.stream.Collectors;

public enum StageType {

    ONE,
    TWO,
    THREE;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void initialize(Plugin plugin) {

        final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("settings.stages");

        switch (this) {
            case ONE -> {
                final ConfigurationSection configurationSectionOne = configurationSection.getConfigurationSection("one");
                this.setStage(new StageImpl(
                        0,
                        configurationSectionOne.getBoolean("pvp"),
                        configurationSectionOne.getInt("duration"),
                        plugin,
                        configurationSectionOne.getStringList("available-abilities").stream().map(string -> AbilityType.valueOf(string.toUpperCase())).collect(Collectors.toList()),
                        configurationSectionOne.getBoolean("open-chest")));
            }
            case TWO -> {
                final ConfigurationSection configurationSectionTwo = configurationSection.getConfigurationSection("two");
                this.setStage(new StageImpl(1,
                        configurationSectionTwo.getBoolean("pvp"),
                        configurationSectionTwo.getInt("duration"),
                        plugin,
                        configurationSectionTwo.getStringList("available-abilities").stream().map(string -> AbilityType.valueOf(string.toUpperCase())).collect(Collectors.toList()),
                        configurationSectionTwo.getBoolean("open-chest")));
            }
            case THREE -> {
                final ConfigurationSection configurationSectionThree = configurationSection.getConfigurationSection("three");
                this.setStage(new StageImpl(2,
                        configurationSectionThree.getBoolean("pvp"),
                        configurationSectionThree.getInt("duration"),
                        plugin,
                        configurationSectionThree.getStringList("available-abilities").stream().map(string -> AbilityType.valueOf(string.toUpperCase())).collect(Collectors.toList()),
                        configurationSectionThree.getBoolean("open-chest")));
            }
        }

    }

}
