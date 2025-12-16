package phrase.towerManaEvent;


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
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import phrase.towerManaEvent.ability.Ability;
import phrase.towerManaEvent.ability.AbilityType;
import phrase.towerManaEvent.event.Chest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SchematicManager {

    private final File file;
    private final Map<Location, Material> blocks;
    private final Location pos1;
    private ProtectedCuboidRegion protectedCuboidRegion;
    private Location pos2;

    public SchematicManager(File file, Location pos1) {
        this.file = file;
        this.blocks = new HashMap<>();
        this.pos1 = pos1;
    }

    public void setSchematic() {

        try(EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(pos1.getWorld()))) {
            Clipboard clipboard = ClipboardFormats.findByFile(file).getReader(new FileInputStream(file)).read();
            this.protectedCuboidRegion = new ProtectedCuboidRegion("TowerManaEvent_" + UUID.randomUUID(), clipboard.getMinimumPoint(), clipboard.getMaximumPoint());
            WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(pos1.getWorld())).addRegion(protectedCuboidRegion);
            BlockVector3 dimensions = clipboard.getDimensions();
            pos2 = new Location(pos1.getWorld(), pos1.getBlockX() + dimensions.getBlockX(), pos1.getBlockY() + dimensions.getBlockY(), pos1.getBlockZ() + dimensions.getBlockZ());
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(pos1.getX(), pos1.getY(), pos1.getZ())).build();
            saveBlocks();
            Operations.complete(operation);
            editSession.flushSession();
        } catch (IOException | WorldEditException e) {
            throw new RuntimeException(e);
        }

    }

    public Chest getChest(Map<AbilityType, Integer> abilities, int mana) {

        World world = pos1.getWorld();

        Chest chest = null;

        for(int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {

            for(int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {

                for(int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {

                    Location location = new Location(world, x, y, z);
                    if(location.getBlock().getType() == Material.CHEST) chest = new Chest(location, abilities, mana);

                }

            }

        }

        return chest;

    }

    private void saveBlocks() {

        World world = pos1.getWorld();

        for(int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {

            for(int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {

                for(int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {

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

        for(int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {

            for(int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {

                for(int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {

                    Location location = new Location(world, x, y, z);
                    Material material = blocks.remove(location);
                    location.getBlock().setType(material);

                }

            }

        }

        WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).removeRegion(protectedCuboidRegion.getId());

    }

    public Location getPos1() {
        return pos1;
    }

}
