package phrase.towerManaEvent.event.ability.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.event.ability.Ability;
import phrase.towerManaEvent.event.Loot;

import java.util.*;

public class SpiderWeb extends Ability {

    private final Set<Location> cachedLocations = new HashSet<>();
    private final Map<Location, Material> savedBlocks = new HashMap<>();

    private final Location location;
    private final int x;
    private final int y;
    private final int z;
    private final int radius;
    private final Plugin plugin;
    private final long laterRemove;
    private final BukkitTask bukkitTask;

    public SpiderWeb(int mana, double damage, Location location, int x, int y, int z, int radius, Plugin plugin, long laterRemove) {
        super("Паутина", mana, damage);
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.plugin = plugin;
        this.laterRemove = laterRemove;

        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {

                cachedLocations.forEach(location -> location.getNearbyEntities(1, 1, 1).forEach(entity -> {

                    if(entity instanceof Player player) {
                        player.damage(damage);
                    }

                }));

            }
        }.runTaskTimer(plugin, 0L, 1L);

    }

    @Override
    public void use(Loot chest) {

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
                        savedBlocks.put(location, location.getBlock().getType());
                        location.getBlock().setType(Material.COBWEB);
                        cachedLocations.add(location);

                    }

                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        cachedLocations.forEach(location -> location.getBlock().setType(savedBlocks.get(location)));
                        cachedLocations.clear();
                        bukkitTask.cancel();
                    }
                }.runTaskLater(plugin, laterRemove);

            }

        }

    }

}
