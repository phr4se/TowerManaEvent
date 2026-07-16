package phrase.towerManaEvent.event.stage;

import org.bukkit.configuration.ConfigurationSection;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.event.ability.AbilityType;
import phrase.towerManaEvent.event.stage.impl.StageImpl;

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

    public void initialize(TowerManaEvent plugin) {
        final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("settings.stages");
        switch (this) {
            case ONE -> {
                final ConfigurationSection configurationSectionOne = configurationSection.getConfigurationSection("one");
                this.setStage(new StageImpl(
                        1,
                        configurationSectionOne.getBoolean("pvp"),
                        configurationSectionOne.getInt("duration"),
                        plugin,
                        configurationSectionOne.getStringList("available-abilities").stream().map(string -> AbilityType.valueOf(string.toUpperCase())).collect(Collectors.toList()),
                        configurationSectionOne.getBoolean("open-chest"),
                        configurationSectionOne.getBoolean("air-or-lighting-drop"),
                        configurationSectionOne.getBoolean("with-lighting")
                        ));
            }
            case TWO -> {
                final ConfigurationSection configurationSectionTwo = configurationSection.getConfigurationSection("two");
                this.setStage(new StageImpl(2,
                        configurationSectionTwo.getBoolean("pvp"),
                        configurationSectionTwo.getInt("duration"),
                        plugin,
                        configurationSectionTwo.getStringList("available-abilities").stream().map(string -> AbilityType.valueOf(string.toUpperCase())).collect(Collectors.toList()),
                        configurationSectionTwo.getBoolean("open-chest"),
                        configurationSectionTwo.getBoolean("air-or-lighting-drop"),
                        configurationSectionTwo.getBoolean("with-lighting")));
            }
            case THREE -> {
                final ConfigurationSection configurationSectionThree = configurationSection.getConfigurationSection("three");
                this.setStage(new StageImpl(3,
                        configurationSectionThree.getBoolean("pvp"),
                        configurationSectionThree.getInt("duration"),
                        plugin,
                        configurationSectionThree.getStringList("available-abilities").stream().map(string -> AbilityType.valueOf(string.toUpperCase())).collect(Collectors.toList()),
                        configurationSectionThree.getBoolean("open-chest"),
                        configurationSectionThree.getBoolean("air-or-lighting-drop"),
                        configurationSectionThree.getBoolean("with-lighting")));
            }
        }
    }
}
