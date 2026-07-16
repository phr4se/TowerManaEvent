package phrase.towerManaEvent.event.ability.impl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.event.Loot;
import phrase.towerManaEvent.event.ability.Ability;

public class Glowing extends Ability {

    private final TowerManaEvent plugin;
    private final double nearbyX, nearbyY, nearbyZ;
    private final int duration;

    public Glowing(int mana, double damage, TowerManaEvent plugin, double nearbyX, double nearbyY, double nearbyZ, int duration) {
        super("Свечение", mana, damage);
        this.plugin = plugin;
        this.nearbyX = nearbyX;
        this.nearbyY = nearbyY;
        this.nearbyZ = nearbyZ;
        this.duration = duration;
    }

    @Override
    public void use(Loot chest) {
        if (chest.getMana() < mana) return;
        chest.subtractMana(mana);
        new BukkitRunnable() {
            final Location location = chest.getLocation();
            final World world = location.getWorld();
            @Override
            public void run() {
                world.getNearbyEntities(location, nearbyX, nearbyY, nearbyZ).stream().filter(entity -> entity instanceof Player).forEach(player -> ((Player) player).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration, 0)));
            }
        }.runTask(plugin);
    }
}
