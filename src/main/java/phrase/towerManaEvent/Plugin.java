package phrase.towerManaEvent;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import phrase.towerManaEvent.command.CommandLogger;
import phrase.towerManaEvent.command.CommandMapper;
import phrase.towerManaEvent.command.CommandResult;
import phrase.towerManaEvent.config.Config;
import phrase.towerManaEvent.event.EventManager;
import phrase.towerManaEvent.event.LootManager;
import phrase.towerManaEvent.event.privilege.PrivilegeManager;
import phrase.towerManaEvent.gui.MenuManager;
import phrase.towerManaEvent.hologram.HologramFactory;
import phrase.towerManaEvent.hologram.HologramProvider;
import phrase.towerManaEvent.listener.ManaListener;
import phrase.towerManaEvent.listener.PlayerListener;
import phrase.towerManaEvent.event.stage.StageManager;
import phrase.towerManaEvent.util.Utils;
import phrase.towerManaEvent.util.colorizer.ColorizerFactory;

public final class Plugin extends JavaPlugin implements CommandExecutor {
    private final CommandLogger commandLogger = new CommandLogger(this);
    private final CommandMapper commandMapper = new CommandMapper(commandLogger, this);
    private final Config config = new Config(this);
    private final StageManager stageManager = new StageManager(this);
    private final MenuManager menuManager = new MenuManager(this);
    private HologramProvider hologramProvider;
    private EventManager eventManager;
    private LootManager lootManager;
    private PrivilegeManager privilegeManager;

    @Override
    public void onEnable() {
        config.setLanguage(config.getDefaultFile("choose-language.yml"));
        config.createFiles("messages.yml", "menus/menu-chances.yml", "chances.yml", "other.yml");
        saveDefaultConfig();
        config.setupSettings();
        Utils.colorizer = ColorizerFactory.getProvider(config.getSettings().colorizerType());
        config.setupMessages();
        config.setupOther();
        config.setupAbilitiesSettings();
        lootManager = new LootManager(config.getSettings().chances(), config.getSettings().items());
        hologramProvider = HologramFactory.getHologramProvider(config.getSettings().hologramType(), this);
        privilegeManager = new PrivilegeManager();
        privilegeManager.setPrivilege(config.getSettings().type(), this);
        eventManager = new EventManager(this);
        getCommand("mana").setExecutor(this);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new ManaListener(this), this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) return true;
            CommandResult commandResult = commandMapper.mapCommand(player, args[0], args);
            if (commandResult.getResultType() != CommandResult.ResultStatus.SUCCESS) {
                if (commandResult.getMessage() != null) {
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

    public MenuManager getMenuManager() {
        return menuManager;
    }
}
