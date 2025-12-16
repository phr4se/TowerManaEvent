package phrase.towerManaEvent.ability.impl;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.Ability;
import phrase.towerManaEvent.event.Chest;

public class SplashPunch extends Ability {

    private final int count;
    private final long laterCount;
    private final int radius;
    private final int stepRadius;
    private final long laterForward;
    private final Plugin plugin;
    private final int particleCount;
    private final long laterForwardParticle;
    private final int step;
    private final Location location;
    private final int x;
    private final int y;
    private final int z;
    private final long laterBack;
    private final long laterBackParticle;

    public SplashPunch(int mana, double damage, int count, long laterCount, int radius, int stepRadius, long laterForward, Plugin plugin, int particleCount, long laterForwardParticle, int step, Location location, int x, int y, int z, long laterBack, long laterBackParticle) {
        super(mana, damage);
        this.count = count;
        this.laterCount = laterCount;
        this.radius = radius;
        this.stepRadius = stepRadius;
        this.laterForward = laterForward;
        this.plugin = plugin;
        this.particleCount = particleCount;
        this.laterForwardParticle = laterForwardParticle;
        this.step = step;
        this.location = location;
        this.x = x;
        this.y = y;
        this.z = z;
        this.laterBack = laterBack;
        this.laterBackParticle = laterBackParticle;
    }

    @Override
    public void use(Chest chest) {

        if(chest.getMana() < mana) return;

        chest.setMana(chest.getMana() - mana);

        for (int i = 1; i <= count; i++) {

            long delayCount = (i - 1) * laterCount;

            new BukkitRunnable() {

                @Override
                public void run() {

                    for (int j = 1; j <= radius; j += stepRadius) {

                        final int currentRadius = j;

                        long delayForward = (j - 1) * laterForward;

                        new BukkitRunnable() {

                            @Override
                            public void run() {

                                for (int k = 1; k <= particleCount; k++) {

                                    int currentParticle = k;

                                    long delayForwardParticle = (k - 1) * laterForwardParticle;

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            double angle = currentParticle * step;
                                            double xParticle = currentRadius * Math.cos(angle);
                                            double zParticle = currentRadius * Math.sin(angle);

                                            Location particleLocation = location.clone().add(xParticle, 0, zParticle);

                                            particleLocation.getNearbyEntities(x, y, z).stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).forEach(player -> {

                                                player.getPersistentDataContainer().set(NamespacedKey.fromString("splash_punch"), PersistentDataType.STRING, "splash_punch");
                                                player.damage(damage);

                                            });

                                            location.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1);

                                        }
                                    }.runTaskLater(plugin, delayForwardParticle);

                                }

                            }

                        }.runTaskLater(plugin, delayForward);


                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int j = radius; j >= 1; j -= stepRadius) {

                                final int currentRadius = j;

                                long delayBack = (radius - j) * laterBack;

                                new BukkitRunnable() {

                                    @Override
                                    public void run() {

                                        for (int k = 1; k <= particleCount; k++) {

                                            long delayBackParticle = (k - 1) * laterBackParticle;

                                            int currentParticle = k;

                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    double angle = currentParticle * step;
                                                    double xParticle = currentRadius * Math.cos(angle);
                                                    double zParticle = currentRadius * Math.sin(angle);

                                                    Location particleLocation = location.clone().add(xParticle, 0, zParticle);

                                                    particleLocation.getNearbyEntities(x, y, z).stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).forEach(player -> {
                                                        player.getPersistentDataContainer().set(NamespacedKey.fromString("splash_punch"), PersistentDataType.STRING, "splash_punch");
                                                        player.damage(damage);
                                                    });

                                                    location.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1);
                                                }
                                            }.runTaskLater(plugin, delayBackParticle);

                                        }
                                    }
                                }.runTaskLater(plugin, delayBack);


                            }
                        }
                    }.runTaskLater(plugin, ((radius - 1) * laterForward + (particleCount - 1) * laterForwardParticle));


                }

            }.runTaskLater(plugin, delayCount);

        }

    }

}
