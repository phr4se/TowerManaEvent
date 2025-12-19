package phrase.towerManaEvent.command.impl;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.command.CommandHandler;
import phrase.towerManaEvent.gui.MenuType;

public class ManaChancesCommand implements CommandHandler {

    private final Plugin plugin;

    public ManaChancesCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean handler(Player player, String[] args) {
        plugin.getMenuManager().showMenu(player, MenuType.MENU_CHANCES);
        return true;
    }

}
