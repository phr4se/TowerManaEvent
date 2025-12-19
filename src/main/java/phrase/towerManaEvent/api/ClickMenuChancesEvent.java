package phrase.towerManaEvent.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ClickMenuChancesEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final InventoryClickEvent event;

    public ClickMenuChancesEvent(Player player, InventoryClickEvent event) {
        this.player = player;
        this.event = event;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getCurrentItem() {
        return event.getCurrentItem();
    }

    public boolean isRightClick() { return event.isRightClick(); }

    public boolean isLeftClick() { return event.isLeftClick(); }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean b) {
        event.setCancelled(b);
    }

}
