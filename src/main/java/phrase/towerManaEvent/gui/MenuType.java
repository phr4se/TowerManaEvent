package phrase.towerManaEvent.gui;

public enum MenuType {
    MENU_CHANCES(0);
    private final int id;

    MenuType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
