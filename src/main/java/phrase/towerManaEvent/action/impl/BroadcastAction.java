package phrase.towerManaEvent.action.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import phrase.towerManaEvent.action.Action;
import phrase.towerManaEvent.action.context.impl.StringContext;
import phrase.towerManaEvent.util.Utils;

public class BroadcastAction implements Action<StringContext> {

    @Override
    public void execute(Player player, StringContext context) {
        Bukkit.getServer().getOnlinePlayers().forEach(target -> Utils.sendMessage(target, context.message()));
    }

}
