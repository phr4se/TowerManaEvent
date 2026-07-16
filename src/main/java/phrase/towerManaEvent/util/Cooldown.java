package phrase.towerManaEvent.util;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Cooldown {

    private final Map<UUID, Material> cooldowns;

    public Cooldown() {
        this.cooldowns = new HashMap<>();
    }

    public void cooldown(UUID player, Material material) {
        cooldowns.put(player, material);
    }

    public boolean hasCooldown(UUID player) {
        return cooldowns.containsKey(player);
    }

    public Material getCooldown(UUID player) {
        return cooldowns.get(player);
    }

    public void removeCooldown(UUID player) {
        cooldowns.remove(player);
    }
}
