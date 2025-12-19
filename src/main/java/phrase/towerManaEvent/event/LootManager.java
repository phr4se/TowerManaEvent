package phrase.towerManaEvent.event;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

public class LootManager {

    private final Map<ItemStack, Double> chances;

    public LootManager(Map<ItemStack, Double> chances) {
        this.chances = chances;
    }

    public void recordItemStack(ItemStack itemStack, double chance) {
        chances.compute(itemStack, (k, v) -> chance);
    }

    public ItemStack[] getRandomLoots(int bound) {
        Random random = new Random();
        return chances.entrySet().stream().filter(entry -> entry.getValue() <= random.nextDouble(bound)).map(Map.Entry::getKey).toArray(ItemStack[]::new);
    }

}
