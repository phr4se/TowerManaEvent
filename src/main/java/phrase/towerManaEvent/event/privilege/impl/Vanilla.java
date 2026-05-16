package phrase.towerManaEvent.event.privilege.impl;

import com.earth2me.essentials.User;
import phrase.towerManaEvent.event.privilege.Privilege;
import phrase.towerManaEvent.event.privilege.PrivilegeChecker;
import phrase.towerManaEvent.event.privilege.PrivilegeDisabler;
import phrase.towerManaEvent.event.privilege.PrivilegeType;

public class Vanilla implements Privilege {
    @Override
    public void initialize() {
        setupFlyChecker();
        setupFlyDisabler();
        setupGodChecker();
        setupGodDisabler();
        setupVanishChecker();
        setupVanishDisabler();
        for (PrivilegeType privilegeType : PrivilegeType.values()) privilegeType.initialize(this);
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
        this.godChecker = player -> player.isInvulnerable();
    }

    @Override
    public PrivilegeChecker godChecker() {
        return godChecker;
    }

    private PrivilegeDisabler godDisabler;

    public void setupGodDisabler() {
        this.godDisabler = player -> player.setInvulnerable(false);
    }

    @Override
    public PrivilegeDisabler godDisabler() {
        return godDisabler;
    }

    private PrivilegeChecker vanishChecker;

    public void setupVanishChecker() {
        this.vanishChecker = player -> player.isInvisible();
    }

    @Override
    public PrivilegeChecker vanishChecker() {
        return vanishChecker;
    }

    private PrivilegeDisabler vanishDisabler;

    public void setupVanishDisabler() {
        this.vanishDisabler = player -> {
            player.setInvisible(true);
        };
    }

    @Override
    public PrivilegeDisabler vanishDisabler() {
        return vanishDisabler;
    }
}
