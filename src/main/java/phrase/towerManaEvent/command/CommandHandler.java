package phrase.towerManaEvent.command;

import org.bukkit.entity.Player;

public interface CommandHandler {
    boolean handler(Player player, String[] args);
}
