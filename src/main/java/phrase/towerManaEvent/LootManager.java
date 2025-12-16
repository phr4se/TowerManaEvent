package phrase.towerManaEvent;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class LootManager {

    private final Map<ItemStack, Double> chances;

    public LootManager(Map<ItemStack, Double> chances) {
        this.chances = chances;
    }

    private ItemStack getRandomLoot() {
        Random random = new Random();
        Optional<ItemStack> optional = chances.entrySet().stream().filter(entry -> entry.getValue() >= random.nextInt(101)).map(Map.Entry::getKey).findFirst();
        return (optional.isPresent()) ? optional.get() : new ItemStack(Material.AIR);
    }

    public ItemStack[] getRandomLoots() {
        ItemStack[] itemStacks = new ItemStack[27];
        for(int i = 0; i < itemStacks.length - 1; i++) {
            itemStacks[i] = getRandomLoot();
        }
        return itemStacks;
    }

}
