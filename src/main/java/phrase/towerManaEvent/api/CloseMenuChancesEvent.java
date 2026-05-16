package phrase.towerManaEvent.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CloseMenuChancesEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Inventory inventory;

    public CloseMenuChancesEvent(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
