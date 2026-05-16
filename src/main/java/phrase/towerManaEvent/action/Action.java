package phrase.towerManaEvent.action;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.action.context.Context;

public interface Action<C extends Context> {
    void execute(Player player, C context);
}
