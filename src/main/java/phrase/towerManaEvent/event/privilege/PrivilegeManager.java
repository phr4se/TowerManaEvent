package phrase.towerManaEvent.event.privilege;

import org.bukkit.entity.Player;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.event.privilege.impl.EssentialsX;
import phrase.towerManaEvent.event.privilege.impl.Vanilla;

public class PrivilegeManager {
    private Privilege privilege;

    public void setPrivilege(String type, Plugin plugin) {
        switch (type.toLowerCase()) {
            case "essentials" -> {
                this.privilege = new EssentialsX(plugin);
                privilege.initialize();
            }
            case "vanilla" -> {
                this.privilege = new Vanilla();
                privilege.initialize();
            }
        }
        for (PrivilegeType privilegeType : PrivilegeType.values()) privilegeType.initialize(privilege);
    }

    public boolean hasPrivilege(Player player) {
        for (PrivilegeType privilegeType : PrivilegeType.values()) {
            if (!privilegeType.getPrivilegeChecker().privilegeChecker(player)) continue;
            return true;
        }
        return false;
    }

    public void disablePrivilege(Player player) {
        for (PrivilegeType privilegeType : PrivilegeType.values()) {
            privilegeType.getPrivilegeDisabler().privilegeDisabler(player);
        }
    }
}
