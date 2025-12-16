package phrase.towerManaEvent.ability;

import phrase.towerManaEvent.event.Chest;

public abstract class Ability {

    protected final int mana;
    protected double damage;

    public Ability(int mana, double damage) {
        this.mana = mana;
        this.damage = damage;
    }

    public abstract void use(Chest chest);

    public void setDamage(double damage) {
        this.damage = damage;
    }

}
