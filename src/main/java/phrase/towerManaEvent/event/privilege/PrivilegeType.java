package phrase.towerManaEvent.event.privilege;

public enum PrivilegeType {

    FLY,
    GOD,
    VANISH;

    private PrivilegeChecker privilegeChecker;
    private PrivilegeDisabler privilegeDisabler;

    public void initialize(Privilege privilege) {

        switch (this) {
            case FLY -> {
                this.privilegeChecker = privilege.flyChecker();
                this.privilegeDisabler = privilege.flyDisabler();
            }
            case GOD -> {
                this.privilegeChecker = privilege.godChecker();
                this.privilegeDisabler = privilege.godDisabler();
            }
            case VANISH -> {
                this.privilegeChecker = privilege.vanishChecker();
                this.privilegeDisabler = privilege.vanishDisabler();
            }
        }

    }

    public PrivilegeChecker getPrivilegeChecker() {
        return privilegeChecker;
    }

    public PrivilegeDisabler getPrivilegeDisabler() {
        return privilegeDisabler;
    }
}

