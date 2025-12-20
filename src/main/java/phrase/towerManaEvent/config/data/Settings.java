package phrase.towerManaEvent.config.data;

import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.inventory.ItemStack;
import phrase.towerManaEvent.event.ability.AbilityType;
import phrase.towerManaEvent.hologram.HologramType;

import java.util.List;
import java.util.Map;

public record Settings(HologramType hologramType, World world, int coordinateRangeX, int coordinateRangeZ, String schematicName, long useAbilities, Map<AbilityType, Integer> abilities, int mana, List<String> hologramLines, Map<String, Double> chances, List<String> actionsStartEvent, List<String> actionsEndEvent, String barMessage, BarColor barColor, BarStyle barStyle, BarFlag[] barFlags, int plusMana, List<String> actionsSwitchStage, String type, List<String> regionFlagsName, int plusManaStage, Map<String, ItemStack> items) {
}
