package phrase.towerManaEvent.event.privilege.impl;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.event.privilege.Privilege;
import phrase.towerManaEvent.event.privilege.PrivilegeChecker;
import phrase.towerManaEvent.event.privilege.PrivilegeDisabler;

public class EssentialsX implements Privilege {
    private Essentials essentials;

    public EssentialsX(Plugin plugin) {
        this.essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
    }

    @Override
    public void initialize() {
        setupFlyChecker();
        setupFlyDisabler();
        setupGodChecker();
        setupGodDisabler();
        setupVanishChecker();
        setupVanishDisabler();
    }

    private PrivilegeChecker flyChecker;

    public void setupFlyChecker() {
        this.flyChecker = player -> player.isFlying();
    }

    @Override
    public PrivilegeChecker flyChecker() {
        return flyChecker;
    }

    private PrivilegeDisabler flyDisabler;

    public void setupFlyDisabler() {
        this.flyDisabler = player -> {
            player.setAllowFlight(false);
            player.setFlying(false);
        };
    }

    @Override
    public PrivilegeDisabler flyDisabler() {
        return flyDisabler;
    }

    private PrivilegeChecker godChecker;

    public void setupGodChecker() {
        this.godChecker = player -> {
            User user = essentials.getUser(player);
            return user.isGodModeEnabled();
        };
    }

    @Override
    public PrivilegeChecker godChecker() {
        return godChecker;
    }

    private PrivilegeDisabler godDisabler;

    public void setupGodDisabler() {
        this.godDisabler = player -> {
            User user = essentials.getUser(player);
            user.setGodModeEnabled(false);
        };
    }

    @Override
    public PrivilegeDisabler godDisabler() {
        return godDisabler;
    }

    private PrivilegeChecker vanishChecker;

    public void setupVanishChecker() {
        this.vanishChecker = player -> {
            User user = essentials.getUser(player);
            return user.isVanished();
        };
    }

    @Override
    public PrivilegeChecker vanishChecker() {
        return vanishChecker;
    }

    private PrivilegeDisabler vanishDisabler;

    public void setupVanishDisabler() {
        this.vanishDisabler = player -> {
            User user = essentials.getUser(player);
            user.setVanished(false);
        };
    }

    @Override
    public PrivilegeDisabler vanishDisabler() {
        return vanishDisabler;
    }
}
