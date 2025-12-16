package phrase.towerManaEvent.stage;

import phrase.towerManaEvent.Plugin;
import phrase.towerManaEvent.ability.AbilityType;

import java.util.List;

public abstract class Stage {

    private final int id;
    protected final boolean pvp;
    protected final Plugin plugin;
    private final int duration;
    protected int remained;
    protected final boolean openChest;

    public Stage(int id, boolean pvp, int duration, Plugin plugin, boolean openChest) {
        this.id = id;
        this.pvp = pvp;
        this.plugin = plugin;
        this.duration = duration;
        this.remained = duration;
        this.openChest = openChest;
    }

    public abstract void setup();

    public abstract List<AbilityType> getAvailableAbilities();

    public int getId() {
        return id;
    }

    public boolean isOpenChest() {
        return openChest;
    }

    public int getDuration() {
        return duration;
    }

    public int getRemained() {
        return remained;
    }

    public boolean isPvp() {
        return pvp;
    }
}
