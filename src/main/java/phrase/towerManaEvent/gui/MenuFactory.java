package phrase.towerManaEvent.gui;

import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.gui.impl.MenuChancesProvider;

public class MenuFactory {

    public static MenuProvider getProvider(MenuType menuType, Plugin plugin) {
        return switch (menuType) {
            case MENU_CHANCES -> new MenuChancesProvider(plugin);
            default -> null;
        };
    }

}
