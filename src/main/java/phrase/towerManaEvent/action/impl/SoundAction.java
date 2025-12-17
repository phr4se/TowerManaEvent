package phrase.towerManaEvent.action.impl;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.action.Action;
import phrase.towerManaEvent.action.context.impl.SoundContext;

public class SoundAction implements Action<SoundContext> {

    @Override
    public void execute(Player player, SoundContext context) {
        player.playSound(player.getLocation(), context.sound(), context.volume(), context.pitch());
    }

}
