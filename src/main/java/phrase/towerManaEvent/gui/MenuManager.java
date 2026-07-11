package phrase.towerManaEvent.gui;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.TowerManaEvent;

public class MenuManager {
    private final TowerManaEvent plugin;

    public MenuManager(TowerManaEvent plugin) {
        this.plugin = plugin;
    }

    public void showMenu(Player player, MenuType menuType) {
        MenuProvider menuProvider = MenuFactory.getProvider(menuType, plugin);
        player.openInventory(menuProvider.getMenu(player));
    }
}
