package phrase.towerManaEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import phrase.towerManaEvent.ability.impl.SpiderWeb;
import phrase.towerManaEvent.command.CommandLogger;
import phrase.towerManaEvent.command.CommandMapper;
import phrase.towerManaEvent.command.CommandResult;
import phrase.towerManaEvent.config.Config;
import phrase.towerManaEvent.event.EventManager;
import phrase.towerManaEvent.event.privilege.PrivilegeManager;
import phrase.towerManaEvent.hologram.HologramFactory;
import phrase.towerManaEvent.hologram.HologramProvider;
import phrase.towerManaEvent.stage.StageManager;
import phrase.towerManaEvent.util.Utils;

public final class Plugin extends JavaPlugin implements CommandExecutor {

    private final CommandLogger commandLogger = new CommandLogger(this);
    private final CommandMapper commandMapper = new CommandMapper(commandLogger, this);
    private final Config config = new Config(this);
    private final StageManager stageManager = new StageManager(this);
    private HologramProvider hologramProvider;
    private EventManager eventManager;
    private LootManager lootManager;
    private PrivilegeManager privilegeManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        config.createFiles("messages.yml");

        config.setupSettings();
        config.setupMessages();
        config.setupAbilitiesSettings();

        lootManager = new LootManager(config.getSettings().chances());
        hologramProvider = HologramFactory.getHologramProvider(config.getSettings().hologramType(), this);
        privilegeManager = new PrivilegeManager();
        privilegeManager.setPrivilege(config.getSettings().type(),this);
        eventManager = new EventManager(this);

        getCommand("mana").setExecutor(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

    }

    @Override
    public void onDisable() {
        SpiderWeb.removeSpiderWeb();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player player) {

            CommandResult commandResult = commandMapper.mapCommand(player, args[0], args);

            if(commandResult.getResultType() != CommandResult.ResultStatus.SUCCESS) {

                if(commandResult.getMessage() != null) {

                    Utils.sendMessage(player, commandResult.getMessage());

                }

            }

        } else Utils.sendMessage(sender, config.getMessages().notAPlayer());


        return true;
    }

    public Config getConfigFile() {
        return config;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public HologramProvider getHologramProvider() {
        return hologramProvider;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public LootManager getLootManager() {
        return lootManager;
    }

    public PrivilegeManager getPrivilegeManager() {
        return privilegeManager;
    }

}
