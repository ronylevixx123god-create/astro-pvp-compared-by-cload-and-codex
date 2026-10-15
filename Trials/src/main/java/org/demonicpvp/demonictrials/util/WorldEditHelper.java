package org.demonicpvp.demonictrials.util;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.world.registry.WorldData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.DemonicTrials;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class WorldEditHelper {

    private static WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
    private static final boolean hasAWE = Bukkit.getPluginManager().getPlugin("AsyncWorldEdit") != null && Bukkit.getPluginManager().getPlugin("AsyncWorldEdit").isEnabled();

    private WorldEditHelper() {}

    public static boolean isAsync() {
        return hasAWE;
    }

    public static WorldEditPlugin getWorldEditPlugin() {
        if (worldEditPlugin == null) {
            worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        }
        return worldEditPlugin;
    }
    static WorldEdit getWorldEdit() {
        getWorldEditPlugin();
        if (worldEditPlugin != null) {
            return worldEditPlugin.getWorldEdit();
        }
        return null;
    }

    public static Selection getSelection(Player player) {
        return getWorldEditPlugin().getSelection(player);
    }


    public static BukkitPlayer getPlayer(Player player) {
        if (player == null) {
            return null;
        }
        return new BukkitPlayer((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"),
                WorldEdit.getInstance().getServer(),
                player);
    }

    public static File getSchematicFile(String fileName, Player player) throws FilenameException {
        WorldEdit we = WorldEdit.getInstance();
        LocalConfiguration config = we.getConfiguration();
        File dir = we.getWorkingDirectoryFile(config.saveDir);
        File f = we.getSafeOpenFile(getPlayer(player), dir, fileName, "schematic", "schematic");

        if (!f.exists()) {
            return null;
        }
        return f;
    }
    public static ClipboardHolder getClipboard(String fileName, World world, Player player) {
        try {
            File file = getSchematicFile(fileName, player);
            if (file == null) {
                return null;
            }
            BukkitWorld bukkitWorld = new BukkitWorld(world);
            ClipboardReader reader = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file));
            WorldData worldData = bukkitWorld.getWorldData();
            Clipboard clipboard = reader.read(null);
            return new ClipboardHolder(clipboard, worldData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void rotate(ClipboardHolder holder, Double yRotate/*, @Optional Double xRotate, @Optional Double zRotate*/) {
        AffineTransform transform = new AffineTransform();
        transform = transform.rotateY(-(yRotate != null ? yRotate : 0));
        //transform = transform.rotateX(-(xRotate != null ? xRotate : 0));
        //transform = transform.rotateZ(-(zRotate != null ? zRotate : 0));
        holder.setTransform(holder.getTransform().combine(transform));
    }

    static com.sk89q.worldedit.world.World getWorld(World world) {
        if (world == null) {
            return null;
        }
        return new BukkitWorld(world);
    }

    @SuppressWarnings("WeakerAccess")
    public static com.sk89q.worldedit.Vector getVector(Location loc) {
        if (loc == null) {
            return null;
        }
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    @SuppressWarnings("WeakerAccess")
    public static Region getRegion(World world, Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null) {
            return null;
        }
        if (world == null) {
            return new CuboidRegion(getVector(pos1), getVector(pos2));
        } else {
            return new CuboidRegion(getWorld(world), getVector(pos1), getVector(pos2));
        }
    }

    public BaseBlock getBlock(Player bukkitPlayer, String block) throws WorldEditException {
        final BukkitPlayer player = getPlayer(bukkitPlayer);
        ParserContext context = new ParserContext();
        context.setActor(player);
        context.setWorld(player.getWorld());

        return getWorldEdit().getBlockFactory().parseFromInput(block, context);
    }

    public static BaseBlock getBlock(Block block) {
        return new BaseBlock(block.getTypeId(), block.getData());
    }
    public static BaseBlock getBlock(Material mat) {
        return getBlock(mat.getId(), 0);
    }
    public static BaseBlock getBlock(Material mat, int data) {
        return getBlock(mat.getId(), data);
    }
    public static BaseBlock getBlock(int id, int data) {
        return new BaseBlock(id, data);
    }

    public static EditSession getEditSession(World world) {
        return getEditSession(world, null);
    }

    public static EditSession getEditSession(World world, Player player) {
        final WorldEdit worldEdit = getWorldEdit();
        if (worldEdit == null) {
            return null;
        }

        final EditSessionFactory sessFactory = worldEdit.getEditSessionFactory();
        final com.sk89q.worldedit.world.World weWorld = WorldEditHelper.getWorld(world);
        return player != null ?
                sessFactory.getEditSession(weWorld, -1, getPlayer(player))
                :
                sessFactory.getEditSession(weWorld, -1);
    }

    public static void pasteClipboard(ClipboardHolder holder, EditSession ses, World world, Vector vector, boolean air) {
        final Operation operation = holder
                .createPaste(ses, new BukkitWorld(world).getWorldData())
                .to(vector)
                .ignoreAirBlocks(air)
                .build();

        Operations.completeBlindly(operation);

    }

    public static void pasteSchematic(String schematicName, Location location) {
        try {
            Schematic schematic = ClipboardFormat.SCHEMATIC.load(new File(DemonicTrials.getInstance().getDataFolder().getAbsoluteFile() + "/../WorldEdit/schematics/" + schematicName + ".schematic"));
            com.sk89q.worldedit.Vector vector = new com.sk89q.worldedit.Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            schematic.paste(new BukkitWorld(location.getWorld()), vector, false, false, null).addNotifyTask(() -> {

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            File file = WorldEditHelper.getSchematicFile(schematicName, null);
            Block targetBlock = location.getBlock();
            com.sk89q.worldedit.Vector pasteLoc = new com.sk89q.worldedit.Vector(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
            ClipboardFormats.findByFile(file).load(file).paste(new BukkitWorld(location.getWorld()), pasteLoc, false, false, null);
        } catch (IOException | FilenameException e) {
            e.printStackTrace();
        }*/
    }


}
