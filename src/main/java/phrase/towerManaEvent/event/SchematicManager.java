package phrase.towerManaEvent.event;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import phrase.towerManaEvent.TowerManaEvent;
import phrase.towerManaEvent.event.ability.AbilityType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SchematicManager {
    private final TowerManaEvent plugin;
    private final File file;
    private final Map<Location, Material> blocks;
    private final List<String> regionFlagsName;
    private ProtectedCuboidRegion protectedCuboidRegion;
    private Location pos1;
    private Location pos2;

    public SchematicManager(TowerManaEvent plugin, File file, List<String> regionFlagsName) {
        this.plugin = plugin;
        this.file = file;
        this.blocks = new HashMap<>();
        this.regionFlagsName = regionFlagsName;
    }

    public void setSchematic(Location pos1) {
        this.pos1 = pos1;
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(pos1.getWorld()))) {
            Clipboard clipboard = ClipboardFormats.findByFile(file).getReader(new FileInputStream(file)).read();
            BlockVector3 dimensions = clipboard.getDimensions();
            pos2 = new Location(pos1.getWorld(), pos1.getBlockX() + dimensions.getBlockX() - 1, pos1.getBlockY() + dimensions.getBlockY(), pos1.getBlockZ() + dimensions.getBlockZ() - 1);
            this.protectedCuboidRegion = new ProtectedCuboidRegion(UUID.randomUUID().toString(), BlockVector3.at(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ()), BlockVector3.at(pos2.getBlockX(), pos2.getBlockY(), pos2.getBlockZ()));
            RegionFlag.setRegionFlags(this.protectedCuboidRegion, regionFlagsName);
            WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(pos1.getWorld())).addRegion(protectedCuboidRegion);
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(pos1.getBlockX(), pos1.getBlockY(), pos1.getBlockZ())).build();
            saveBlocks();
            Operations.complete(operation);
            editSession.flushSession();
        } catch (IOException | WorldEditException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Location, Loot> setChests(Map<AbilityType, Integer> abilities, int mana) {
        final Map<Location, Loot> loots = new HashMap<>();
        final ConfigurationSection configurationSection = plugin.getConfig().getConfigurationSection("settings.chests");
        configurationSection.getKeys(false).forEach(key -> {
            int offsetX = configurationSection.getInt(key + ".offset-x");
            int offsetY = configurationSection.getInt(key + ".offset-y");
            int offsetZ = configurationSection.getInt(key + ".offset-z");
            Location location = new Location(pos1.getWorld(), pos1.getX() + offsetX, pos1.getY() + offsetY, pos1.getZ() + offsetZ);
            int offsetPitch = configurationSection.getInt(key + ".offset-pitch");
            location.setPitch(offsetPitch);
            location.getBlock().setType(Material.CHEST);
            Inventory inventory = null;
            if (location.getBlock().getState() instanceof Chest chest) inventory = chest.getInventory();
            loots.put(location, new Loot(inventory, location, abilities, mana));
        });
        return loots;
    }

    private void saveBlocks() {
        World world = pos1.getWorld();
        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {
                for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                    Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();
                    blocks.put(location, block.getType());
                    block.setType(Material.AIR);
                }
            }
        }
    }

    public void regenerationBlocks() {
        World world = pos1.getWorld();
        for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
            for (int y = pos1.getBlockY() - 1; y <= pos2.getBlockY(); y++) {
                for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                    Location location = new Location(world, x, y, z);
                    Material material;
                    if (!blocks.containsKey(location)) material = Material.AIR;
                    else material = blocks.remove(location);
                    location.getBlock().setType(material);
                }
            }
        }
        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).removeRegion(protectedCuboidRegion.getId());
    }

    public void setPvp(boolean pvp) {
        if (pvp) protectedCuboidRegion.setFlag(Flags.PVP, StateFlag.State.ALLOW);
        else protectedCuboidRegion.setFlag(Flags.PVP, StateFlag.State.DENY);
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public ProtectedCuboidRegion getProtectedCuboidRegion() {
        return protectedCuboidRegion;
    }

    public boolean existsSchematic() {
        return file.exists();
    }

    public boolean schematicDamaged() {
        return ClipboardFormats.findByFile(file) == null;
    }
}
