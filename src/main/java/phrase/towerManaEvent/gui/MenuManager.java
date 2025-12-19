package phrase.towerManaEvent.gui;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.Plugin;

public class MenuManager {

    private final Plugin plugin;

    public MenuManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void showMenu(Player player, MenuType menuType) {

        MenuProvider menuProvider = MenuFactory.getProvider(menuType, plugin);

        player.openInventory(menuProvider.getMenu(player));

    }

}
