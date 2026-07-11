package phrase.towerManaEvent.gui;

import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.gui.impl.MenuChancesProvider;

public class MenuFactory {
    public static MenuProvider getProvider(MenuType menuType, TowerManaEvent plugin) {
        return switch (menuType) {
            case MENU_CHANCES -> new MenuChancesProvider(plugin);
            default -> null;
        };
    }
}
