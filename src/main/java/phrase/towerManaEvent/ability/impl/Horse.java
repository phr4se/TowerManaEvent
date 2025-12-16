package phrase.towerManaEvent.ability.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.Ability;
import phrase.towerManaEvent.event.Chest;

import java.util.HashMap;
import java.util.Map;

public class Horse extends Ability {

    private final Location location;
    private final int distance;
    private final int num1;
    private final int num2;
    private final Plugin plugin;
    private final int forwardBlocks;
    private final double speed;
    private final int knockbackBlocks;
    private final long laterDeath;

    public Horse(int mana, double damage, Location location, int distance, int num1, int num2, Plugin plugin, int forwardBlocks, double speed, int knockbackBlocks, long laterDeath) {
        super(mana, damage);
        this.location = location;
        this.distance = distance;
        this.num1 = num1;
        this.num2 = num2;
        this.plugin = plugin;
        this.forwardBlocks = forwardBlocks;
        this.speed = speed;
        this.knockbackBlocks = knockbackBlocks;
        this.laterDeath = laterDeath;
    }

    @Override
    public void use(Chest chest) {

        if(chest.getMana() < mana) return;

        chest.setMana(chest.getMana() - mana);

        Vector perpendicular = new Vector(-location.getDirection().getZ(), 0, location.getDirection().getX());

        Map<Location, SkeletonHorse> skeletonHorses = new HashMap<>();

        for (int i = num1; i < num2; i++) {

            double offsetFromCenter = i * distance;

            Location offsetLocation = location.clone().add(perpendicular.clone().multiply(offsetFromCenter));

            long yValue = (long) offsetLocation.getY();

            if(offsetLocation.getY() != (double) yValue) {
                offsetLocation.setY((int) Math.floor(offsetLocation.getY()));
            }

            while((offsetLocation.clone().add(0, -1, 0).getBlock().getType()) == Material.AIR) offsetLocation.add(0, -1, 0);

            SkeletonHorse skeletonHorse = location.getWorld().spawn(offsetLocation, SkeletonHorse.class);

            skeletonHorse.setGliding(false);
            Location finishPosition = offsetLocation.add(location.getDirection().multiply(forwardBlocks));
            skeletonHorses.put(finishPosition, skeletonHorse);

        }

        new BukkitRunnable() {

            @Override
            public void run() {

                skeletonHorses.entrySet().forEach(entry -> {
                    Location finishPosition = entry.getKey();
                    SkeletonHorse skeletonHorse = entry.getValue();
                    Location startPosition = skeletonHorse.getLocation();


                    for (Entity entity : startPosition.getNearbyEntities(1, 1, 1)) {
                        if (entity instanceof Player player) {

                            player.setVelocity(player.getLocation().getDirection().normalize().multiply(-knockbackBlocks));
                            player.damage(damage);

                        }
                    }

                    double toMoveX = 0, toMoveY = 0, toMoveZ = 0;

                    if (Math.abs(startPosition.getX() - finishPosition.getX()) > 0.2) {
                        if (startPosition.getX() > finishPosition.getX()) {
                            toMoveX = -speed;
                        } else {
                            toMoveX = speed;
                        }
                    }

                    if (Math.abs(startPosition.getY() - finishPosition.getY()) >= 1.0) {
                        if (startPosition.getY() > finishPosition.getY()) {
                            toMoveY = -1.0;
                        } else {
                            toMoveY = 1.0;
                        }
                    }

                    if (Math.abs(startPosition.getZ() - finishPosition.getZ()) > 0.2) {
                        if (startPosition.getZ() > finishPosition.getZ()) {
                            toMoveZ = -speed;
                        } else {
                            toMoveZ = speed;
                        }
                    }

                    skeletonHorse.setVelocity(new Vector(toMoveX, toMoveY, toMoveZ));
                });

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        skeletonHorses.entrySet().forEach(entry -> {
                            SkeletonHorse skeletonHorse = entry.getValue();
                            skeletonHorse.setHealth(0.0);
                        });
                        skeletonHorses.clear();
                    }
                }.runTaskLater(plugin, laterDeath);

            }
        }.runTaskTimer(plugin, 0L, 1L);


    }

}
