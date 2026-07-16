package phrase.towerManaEvent.util;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import phrase.towerManaEvent.util.colorizer.ColorizerProvider;

import java.util.HashSet;
import java.util.Set;

public class Utils {
    public static ColorizerProvider colorizer;

    public static void sendMessage(CommandSender commandSender, String message) {
        commandSender.sendMessage(message);
    }

    public static void sendMessage(Player player, String message) {
        if (player == null || message == null) return;
        player.sendMessage(message);
    }
}
