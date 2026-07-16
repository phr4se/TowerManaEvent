package phrase.towerManaEvent.util;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaskedRealType {

    public static final Set<Material> MASK = new HashSet<>();

    static {
        MASK.add(Material.WHITE_DYE);
        MASK.add(Material.GRAY_DYE);
        MASK.add(Material.LIGHT_GRAY_DYE);
    }

    private final Map<Integer, Material> masked;

    public MaskedRealType() {
        this.masked = new HashMap<>();
    }

    public void mask(int slot, Material material) {
        masked.put(slot, material);
    }

    public Material getRealType(int slot) {
        return masked.remove(slot);
    }

    public boolean isMasked(int slot) {
        return masked.containsKey(slot);
    }
}
