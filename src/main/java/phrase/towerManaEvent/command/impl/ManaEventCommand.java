package phrase.towerManaEvent.command.impl;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.command.CommandHandler;

public class ManaEventCommand implements CommandHandler {

    private final Plugin plugin;

    public ManaEventCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean handler(Player player, String[] args) {
        plugin.getEventManager().startEvent();
        return true;
    }

}
