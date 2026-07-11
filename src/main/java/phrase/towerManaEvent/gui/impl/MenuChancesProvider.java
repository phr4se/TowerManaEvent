package phrase.towerManaEvent.gui.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.gui.MenuProvider;

public class MenuChancesProvider extends MenuProvider {
    public MenuChancesProvider(TowerManaEvent plugin) {
        super(new MenuChancesService(plugin));
    }

    @Override
    public Inventory getMenu(Player player) {
        return getMenuService().create(player);
    }
}
