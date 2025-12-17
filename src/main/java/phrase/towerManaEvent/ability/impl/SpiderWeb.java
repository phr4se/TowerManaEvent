package phrase.towerManaEvent.ability.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.Ability;
import phrase.towerManaEvent.event.Chest;

import java.util.*;

public class SpiderWeb extends Ability {

    private final static Set<Location> CACHED_LOCATIONS = new HashSet<>();
    private final static Map<Location, Material> SAVED_BLOCKS = new HashMap<>();

    public static void removeSpiderWeb() {
        CACHED_LOCATIONS.forEach(location -> location.getBlock().setType(SAVED_BLOCKS.get(location)));
    }

    private final Location location;
    private final int x;
    private final int y;
    private final int z;
    private final int radius;
    private final Plugin plugin;
    private final long laterRemove;

    public SpiderWeb(int mana, double damage, Location location, int x, int y, int z, int radius, Plugin plugin, long laterRemove) {
        super(mana, damage);
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.plugin = plugin;
        this.laterRemove = laterRemove;

        new BukkitRunnable() {
            @Override
            public void run() {

                CACHED_LOCATIONS.forEach(location -> location.getNearbyEntities(1, 1, 1).forEach(entity -> {

                    if(entity instanceof Player player) {
                        player.damage(damage);
                    }

                }));

            }
        }.runTaskTimer(plugin, 0L, 1L);

    }

    @Override
    public void use(Chest chest) {

        if(chest.getMana() < mana) return;

        chest.subtractMana(mana);

        for (Entity entity : location.getNearbyEntities(x, y, z)) {

            if (entity instanceof Player) {

                World world = entity.getLocation().getWorld();

                Location entityLocation = entity.getLocation();
                int y = entityLocation.getBlockY();

                for (int x = entityLocation.getBlockX() - radius; x < entityLocation.getBlockX() + radius; x++) {

                    for (int z = entityLocation.getBlockZ() - radius; z < entityLocation.getBlockZ() + radius; z++) {

                        Location location = new Location(world, x, y, z);
                        if (location.getBlock().getType() != Material.AIR) continue;
                        SAVED_BLOCKS.put(location, location.getBlock().getType());
                        location.getBlock().setType(Material.COBWEB);
                        CACHED_LOCATIONS.add(location);

                    }

                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        CACHED_LOCATIONS.forEach(location -> location.getBlock().setType(SAVED_BLOCKS.get(location)));
                    }
                }.runTaskLater(plugin, laterRemove);

            }

        }

    }

}
