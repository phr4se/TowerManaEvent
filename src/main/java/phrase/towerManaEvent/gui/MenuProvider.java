package phrase.towerManaEvent.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class MenuProvider {
    private final MenuService menuService;

    public MenuProvider(MenuService menuService) {
        this.menuService = menuService;
    }

    protected MenuService getMenuService() {
        return menuService;
    }

    public abstract Inventory getMenu(Player player);
}
