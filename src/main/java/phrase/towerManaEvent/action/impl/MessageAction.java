package phrase.towerManaEvent.action.impl;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.action.Action;

import phrase.towerManaEvent.action.context.impl.StringContext;
import phrase.towerManaEvent.util.Utils;

public class MessageAction implements Action<StringContext> {

    @Override
    public void execute(Player player, StringContext context) {
        Utils.sendMessage(player, context.message());
    }

}
