package phrase.towerManaEvent.event.privilege;

public interface Privilege {
    void initialize();
    PrivilegeChecker flyChecker();
    PrivilegeDisabler flyDisabler();
    PrivilegeChecker godChecker();
    PrivilegeDisabler godDisabler();
    PrivilegeChecker vanishChecker();
    PrivilegeDisabler vanishDisabler();
}
