package phrase.towerManaEvent.event;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class LootManager {
    private final Map<String, Double> chances;
    private final Map<String, ItemStack> items;

    public LootManager(Map<String, Double> chances, Map<String, ItemStack> items) {
        this.chances = chances;
        this.items = items;
    }

    public void add(String key, ItemStack itemStack, double chance) {
        chances.put(key, chance);
        items.put(key, itemStack);
    }

    public void recordChance(String key, double chance) {
        chances.compute(key, (k, v) -> chance);
    }

    public ItemStack[] getRandomLoots(int bound) {
        Random random = new Random();
        Map<ItemStack, Double> map = new HashMap<>();
        items.entrySet().forEach(entry -> map.put(entry.getValue(), chances.get(entry.getKey())));
        return map.entrySet().stream().filter(entry -> random.nextDouble(bound) < entry.getValue()).map(Map.Entry::getKey).map(itemStack -> {
            ItemStack newItemStack = itemStack.clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            if (persistentDataContainer.has(NamespacedKey.fromString("towermanaevent_key"), PersistentDataType.STRING)) persistentDataContainer.remove(NamespacedKey.fromString("towermanaevent_key"));
            newItemStack.setItemMeta(itemMeta);
            return newItemStack;
        }).toArray(ItemStack[]::new);
    }

    public double getChance(String key) {
        return chances.compute(key, (k, v) -> (v == null) ? 0 : v);
    }

    public Map<String, Double> getChances() {
        return Collections.unmodifiableMap(chances);
    }

    public Map<String, ItemStack> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
