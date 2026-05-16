package phrase.towerManaEvent.event.ability;

import phrase.towerManaEvent.event.Loot;

public abstract class Ability {
    private final String name;
    protected final int mana;
    protected double damage;

    public Ability(String name, int mana, double damage) {
        this.name = name;
        this.mana = mana;
        this.damage = damage;
    }

    public abstract void use(Loot chest);

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }
}
