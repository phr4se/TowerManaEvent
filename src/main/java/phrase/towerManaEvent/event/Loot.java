package phrase.towerManaEvent.event;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import phrase.towerManaEvent.event.ability.AbilityType;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class Loot {
    private final UUID uuid;
    private final Inventory inventory;
    private final Location location;
    private final Map<AbilityType, Integer> abilities;
    private int mana;

    public Loot(Inventory inventory, Location location, Map<AbilityType, Integer> abilities, int mana) {
        this.uuid = UUID.randomUUID();
        this.inventory = inventory;
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

    public Inventory getInventory() {
        return inventory;
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

    public UUID getUuid() {
        return uuid;
    }
}
