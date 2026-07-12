package phrase.towerManaEvent.event.stage;

import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.event.ability.Ability;
import phrase.towerManaEvent.event.ability.AbilityType;

import java.util.List;

public abstract class Stage {
    private final int id;
    protected final boolean pvp;
    protected final TowerManaEvent plugin;
    private final int duration;
    protected int remained;
    protected final boolean openChest;
    private Ability latestUsedAbility;

    public Stage(int id, boolean pvp, int duration, TowerManaEvent plugin, boolean openChest) {
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

    public void setRemained(int remained) {
        this.remained = remained;
    }

    public int getRemained() {
        return remained;
    }

    public boolean isPvp() {
        return pvp;
    }

    public void setLatestUsedAbility(Ability latestUsedAbility) {
        this.latestUsedAbility = latestUsedAbility;
    }

    public Ability getLatestUsedAbility() {
        return latestUsedAbility;
    }
}
