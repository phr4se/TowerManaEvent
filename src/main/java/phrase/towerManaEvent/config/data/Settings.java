package phrase.towerManaEvent.config.data;

import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.inventory.ItemStack;
import phrase.towerManaEvent.event.ability.AbilityType;
import phrase.towerManaEvent.hologram.HologramType;
import phrase.towerManaEvent.util.colorizer.ColorizerType;

import java.util.List;
import java.util.Map;

public record Settings(ColorizerType colorizerType, HologramType hologramType, World world, int coordinateRangeX,
                       int coordinateRangeZ,
                       String schematicName, long useAbilities, Map<AbilityType, Integer> abilities, int mana,
                       Map<String, Double> chances, BarColor barColor, BarStyle barStyle,
                       BarFlag[] barFlags, int plusMana, String type,
                       List<String> regionFlagsName, int plusManaStage, Map<String, ItemStack> items) {
}
