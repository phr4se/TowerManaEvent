package phrase.towerManaEvent.ability.impl;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.Ability;
import phrase.towerManaEvent.event.Chest;

import java.util.HashSet;
import java.util.Set;

public class Fireball extends Ability {

    private final Location location;
    private final int x;
    private final int y;
    private final int z;
    private final int countFireball;
    private final int boostY;
    private final int offsetLocationX;
    private final int offsetLocationZ;
    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;
    private final int speed;
    private final Plugin plugin;

    public Fireball(int mana, double damage, Location location, int x, int y, int z, int countFireball, int boostY, int offsetLocationX, int offsetLocationZ, int offsetX, int offsetY, int offsetZ, int speed, Plugin plugin) {
        super(mana, damage);
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
        this.countFireball = countFireball;
        this.boostY = boostY;
        this.offsetLocationX = offsetLocationX;
        this.offsetLocationZ = offsetLocationZ;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.plugin = plugin;
    }

    @Override
    public void use(Chest chest) {

        if(chest.getMana() < mana) return;

        chest.subtractMana(mana);

        Set<Player> players = new HashSet<>();
        for(Entity entity : location.getNearbyEntities(x, y, z)) {
            if(entity instanceof Player player) {
                players.add(player);
            }
        }
        if(players.isEmpty()) return;
        int fireballForPlayer = (int) Math.ceil((double) countFireball / players.size());
        for (Player player : players) {
            Location fireballLocation1 = location.clone().add(0, boostY, 0);

            World world = fireballLocation1.getWorld();

            Location fireballLocation2 = location.clone().add(offsetLocationX, 0, offsetLocationZ);

            for (int i = 0; i < fireballForPlayer / 2; i++) {

                Location offsetFireballLocation1 = fireballLocation1.add(offsetX, offsetY, offsetZ);
                org.bukkit.entity.Fireball fireball1 = (org.bukkit.entity.Fireball) world.spawnEntity(offsetFireballLocation1, EntityType.FIREBALL);

                Location offsetFireballLocation2 = fireballLocation2.add(offsetX, offsetY, offsetZ);
                org.bukkit.entity.Fireball fireball2 = (org.bukkit.entity.Fireball) world.spawnEntity(offsetFireballLocation2, EntityType.FIREBALL);

                fireball1.getPersistentDataContainer().set(NamespacedKey.fromString("boss_abilities_fireball"), PersistentDataType.DOUBLE, damage);
                fireball2.getPersistentDataContainer().set(NamespacedKey.fromString("boss_abilities_fireball"), PersistentDataType.DOUBLE, damage);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if (fireball1.isDead() && fireball2.isDead()) {
                            cancel();
                            return;
                        }

                        if (!fireball1.isDead() && !fireball2.isDead()) {
                            Vector fireballVector1 = player.getEyeLocation().toVector().subtract(fireball1.getLocation().toVector()).normalize().multiply(speed);
                            fireball1.setDirection(fireballVector1);

                            Vector fireballVector2 = player.getEyeLocation().toVector().subtract(fireball2.getLocation().toVector()).normalize().multiply(speed);
                            fireball2.setDirection(fireballVector2);
                        }

                        if (!fireball1.isDead()) {
                            Vector fireballVector1 = player.getEyeLocation().toVector().subtract(fireball1.getLocation().toVector()).normalize().multiply(speed);
                            fireball1.setDirection(fireballVector1);
                        }

                        if (!fireball2.isDead()) {
                            Vector fireballVector2 = player.getEyeLocation().toVector().subtract(fireball2.getLocation().toVector()).normalize().multiply(speed);
                            fireball2.setDirection(fireballVector2);
                        }

                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }
        }

    }

}
