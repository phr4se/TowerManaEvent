package phrase.towerManaEvent.command.impl;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.command.CommandHandler;
import phrase.towerManaEvent.gui.MenuType;
import phrase.towerManaEvent.util.Utils;

public class ManaChancesCommand implements CommandHandler {
    private final Plugin plugin;

    public ManaChancesCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean handler(Player player, String[] args) {
        plugin.getMenuManager().showMenu(player, MenuType.MENU_CHANCES);
        plugin.getConfigFile().getCommandMessages().manual().forEach(message -> Utils.sendMessage(player, message));
        return true;
    }
}
