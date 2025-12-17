package phrase.towerManaEvent.event;

import org.bukkit.Location;
import phrase.towerManaEvent.ability.AbilityType;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Chest {

    private final Location location;
    private final Map<AbilityType, Integer> abilities;
    private int mana;

    public Chest(Location location, Map<AbilityType, Integer> abilities, int mana) {
        this.location = location;
        this.abilities = abilities;
        this.mana = mana;
    }

    public Location getLocation() {
        return location;
    }

    public AbilityType getRandomAbility() {
        return abilities.keySet().stream().collect(Collectors.toList()).get(new Random().nextInt(abilities.keySet().size()));
    }

    public int getAbilityMana(AbilityType abilityType) {
        return abilities.get(abilityType);
    }

    public void addMana(int mana) {
        this.mana += mana;
    }

    public void subtractMana(int mana) {
        this.mana -= mana;
    }

    public int getMana() {
        return mana;
    }

}
