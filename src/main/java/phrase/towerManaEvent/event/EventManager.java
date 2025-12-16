package phrase.towerManaEvent.event;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.SchematicManager;
import phrase.towerManaEvent.ability.Ability;
import phrase.towerManaEvent.ability.AbilityType;
import phrase.towerManaEvent.ability.impl.Fireball;
import phrase.towerManaEvent.ability.impl.Horse;
import phrase.towerManaEvent.ability.impl.SpiderWeb;
import phrase.towerManaEvent.ability.impl.SplashPunch;
import phrase.towerManaEvent.config.Config;
import phrase.towerManaEvent.config.data.*;
import phrase.towerManaEvent.hologram.HologramProvider;
import phrase.towerManaEvent.stage.Stage;
import phrase.towerManaEvent.stage.StageManager;
import phrase.towerManaEvent.util.Utils;

import java.io.File;
import java.util.List;
import java.util.Random;

public class EventManager {

    private final Plugin plugin;
    private final HologramProvider hologramProvider;
    private SchematicManager schematicManager;
    private Stage stage;
    private Chest chest;
    private BukkitTask bukkitTaskUseAbilities;
    private BukkitTask bukkitTaskHologram;
    private boolean eventRunning;

    public EventManager(Plugin plugin) {
        this.plugin = plugin;
        this.hologramProvider = plugin.getHologramProvider();
    }

    public void startEvent() {

        new BukkitRunnable() {

            @Override
            public void run() {

                Config config = plugin.getConfigFile();
                Settings settings = config.getSettings();

                World world = settings.world();

                Location location;
                Random random = new Random();

                boolean availableCoordinates;

                RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));

                do {

                    int x = random.nextInt(settings.coordinateRangeX());
                    int z = random.nextInt(settings.coordinateRangeZ());
                    int y = world.getHighestBlockYAt(x, z);

                    location = new Location(world, x, y, z);
                    if(location.getBlock().getType() == Material.WATER || location.getBlock().getType() == Material.LAVA) location.add(0, 2, 0);

                    availableCoordinates = regionManager.getApplicableRegions(BlockVector3.at(location.getX(), location.getY(), location.getZ())).getRegions().isEmpty();

                } while (!availableCoordinates);

                StageManager stageManager = plugin.getStageManager();
                stage = stageManager.getStages().get(0);

                Location finalLocation = location;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        eventRunning = true;

                        schematicManager = new SchematicManager(new File(plugin.getDataFolder() + "/schematics/" + settings.schematicName()), finalLocation);
                        hologramProvider.createHologram(finalLocation.clone().add(0.5, 2, 0.5), settings.hologramLines());
                        chest = schematicManager.getChest(settings.abilities(), settings.mana());
                        stage.setup();
                        startTaskUseAbilities();
                        broadcastMessages(settings.messagesStartEvent());
                        enableBossBar();

                    }
                }.runTask(plugin);

            }

        }.runTaskAsynchronously(plugin);

    }

    private void startTaskUseAbilities() {

        Config config = plugin.getConfigFile();

        HorseSettings horseSettings = config.getHorseSettings();
        FireballSettings fireballSettings = config.getFireballSettings();
        SpiderWebSettings spiderWebSettings = config.getSpiderWebSettings();
        SplashPunchSettings splashPunchSettings = config.getSplashPunchSettings();

        bukkitTaskUseAbilities = new BukkitRunnable() {
            @Override
            public void run() {

                AbilityType abilityType;
                do {
                    abilityType = chest.getRandomAbility();
                } while (!stage.getAvailableAbilities().contains(abilityType));
                int mana = chest.getAbilityMana(abilityType);

                AbilityType finalAbilityType = abilityType;
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        switch (finalAbilityType) {
                            case HORSE -> {
                                Ability ability = new Horse(mana, horseSettings.damage(), chest.getLocation(), horseSettings.distance(), horseSettings.num1(), horseSettings.num2(), plugin, horseSettings.forwardBlocks(), horseSettings.speed(), horseSettings.knockbackBlocks(), horseSettings.laterDeath());
                                ability.use(chest);
                            }
                            case FIREBALL -> {
                                Ability ability = new Fireball(mana, fireballSettings.damage(), chest.getLocation(), fireballSettings.radiusSearchPlayers(), fireballSettings.radiusSearchPlayers(), fireballSettings.radiusSearchPlayers(), fireballSettings.countFireball(), fireballSettings.boostY(), fireballSettings.offsetLocationX(), fireballSettings.offsetLocationZ(), fireballSettings.offsetX(), fireballSettings.offsetY(), fireballSettings.offsetZ(), fireballSettings.speed(), plugin);
                                ability.use(chest);
                            }
                            case SPIDER_WEB -> {
                                Ability ability = new SpiderWeb(mana, spiderWebSettings.damage(), chest.getLocation(), spiderWebSettings.radiusSearchPlayers(), spiderWebSettings.radiusSearchPlayers(), spiderWebSettings.radiusSearchPlayers(), spiderWebSettings.radius(), plugin, spiderWebSettings.laterRemove());
                                ability.use(chest);
                            }
                            case SPLASH_PUNCH -> {
                                Ability ability = new SplashPunch(mana, splashPunchSettings.damage(), splashPunchSettings.count(), splashPunchSettings.laterCount(), splashPunchSettings.radius(), splashPunchSettings.stepRadius(), splashPunchSettings.laterForward(), plugin, splashPunchSettings.particleCount(), splashPunchSettings.laterForwardParticle(), splashPunchSettings.step(), chest.getLocation(), splashPunchSettings.radiusSearchPlayers(), splashPunchSettings.radiusSearchPlayers(), splashPunchSettings.radiusSearchPlayers(), splashPunchSettings.laterBack(), splashPunchSettings.laterBackParticle());
                                ability.use(chest);
                            }
                        }

                    }
                }.runTask(plugin);

            }
        }.runTaskTimerAsynchronously(plugin, 0L, plugin.getConfigFile().getSettings().useAbilities());

    }

    private void stopEvent() {

        schematicManager.regenerationBlocks();
        bukkitTaskUseAbilities.cancel();
        hologramProvider.removeHologram();

        eventRunning = false;
        broadcastMessages(plugin.getConfigFile().getSettings().messagesEndEvent());
        disableBossBar();

    }

    private void broadcastMessages(List<String> messages) {
        plugin.getServer().getOnlinePlayers().forEach(player -> messages.forEach(message -> Utils.sendMessage(player, replacePlaceholder(message))));
    }

    private String replacePlaceholder(String message) {
        Location pos = schematicManager.getPos1();
        return message.replace("%x%", String.valueOf(pos.getX()))
                .replace("%y%", String.valueOf(pos.getY()))
                .replace("%z%", String.valueOf(pos.getZ()));
    }

    private void enableBossBar() {

        Server server = plugin.getServer();

        BossBar bossBar = null;

        String barMessage = plugin.getConfigFile().getSettings().barMessage();
        if(server.getBossBar(NamespacedKey.fromString("TowerManaEvent_BossBar")) == null) {
            Settings settings = plugin.getConfigFile().getSettings();
            bossBar = server.createBossBar(NamespacedKey.fromString("TowerManaEvent_BossBar"), replacePlaceholderBossBar(barMessage), settings.barColor(), settings.barStyle(), settings.barFlags());
        } else {
            bossBar = server.getBossBar(NamespacedKey.fromString("TowerManaEvent_BossBar"));
        }

        bossBar.setVisible(true);

        BossBar finalBossBar = bossBar;
        bukkitTaskHologram = new BukkitRunnable() {
            @Override
            public void run() {

                finalBossBar.setTitle(replacePlaceholderBossBar(barMessage));
                finalBossBar.setProgress((double) stage.getDuration() / stage.getRemained());
                final List<Player> players = finalBossBar.getPlayers();
                server.getOnlinePlayers().forEach(player -> {
                    if(!players.contains(player)) finalBossBar.addPlayer(player);
                });

            }
        }.runTaskTimer(plugin, 0L, 1L);

    }

    private String replacePlaceholderBossBar(String barMessage) {
        Location pos = schematicManager.getPos1();
        return Utils.COLORIZER.colorize(barMessage.replace("%x%", String.valueOf(pos.getX()))
                .replace("%y%", String.valueOf(pos.getY()))
                .replace("%z%", String.valueOf(pos.getZ()))
                .replace("%stage%", String.valueOf(stage.getId()))
                .replace("%pvp%", String.valueOf(stage.isPvp()))
                .replace("%remaining%", String.valueOf(stage.getRemained())));
    }

    private void disableBossBar() {
        bukkitTaskHologram.cancel();
        BossBar bossBar = plugin.getServer().getBossBar(NamespacedKey.fromString("TowerManaEvent_BossBar"));
        bossBar.setVisible(false);
        bossBar.removeAll();
    }

    public void switchStage() {
        this.stage = plugin.getStageManager().getNextStage(stage);
        if(stage != null) stage.setup();
        else stopEvent();
    }

    public boolean isEventRunning() {
        return eventRunning;
    }

    public Chest getChest() {
        return chest;
    }

    public Stage getStage() {
        return stage;
    }
}
