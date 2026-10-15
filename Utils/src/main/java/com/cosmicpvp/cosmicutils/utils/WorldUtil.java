package com.cosmicpvp.cosmicutils.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class WorldUtil {
    public static void sendWorldEffect(Block b, int effectID) {
        PacketPlayOutWorldEvent particles = new PacketPlayOutWorldEvent(effectID, new BlockPosition(b.getX(), b.getY(), b.getZ()), b.getTypeId(), false);
        ParticleEffect.sendWorldEventPacket(particles, b.getLocation());
    }

    public static void sendWorldEffect(Block b, int materialID, int effectID) {
        PacketPlayOutWorldEvent particles = new PacketPlayOutWorldEvent(effectID, new BlockPosition(b.getX(), b.getY(), b.getZ()), materialID, false);
        ParticleEffect.sendWorldEventPacket(particles, b.getLocation());
    }

    public static void sendBlockBreakAnimation(Block block, int breaks, List<Player> players) {
        PacketPlayOutBlockBreakAnimation animation = new PacketPlayOutBlockBreakAnimation(0, new BlockPosition(block.getX(), block.getY(), block.getZ()), breaks);
        for (Player pl : players) {
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(animation);
        }
    }

    public static void sendWorldBreakPacket(Block b) {
        PacketPlayOutWorldEvent particles = new PacketPlayOutWorldEvent(2001, new BlockPosition(b.getX(), b.getY(), b.getZ()), b.getTypeId(), false);
        ParticleEffect.sendWorldEventPacket(particles, b.getLocation());
    }

    public static boolean allowEnderPortalOutsideOverworld(boolean allowed) {
        return allowed;
    }

    public static boolean forceUnloadWorld(World world, boolean save) throws NoSuchFieldException, IllegalAccessException {
        if (world == null) {
            return false;
        }
        WorldServer handle = ((CraftWorld) world).getHandle();
        LinkedHashMap worlds = (LinkedHashMap) ReflectionUtils.getObject(CraftServer.class, (Object) handle.getServer(), "worlds");
        MinecraftServer console = handle.getMinecraftServer();
        WorldUnloadEvent e = new WorldUnloadEvent((World) handle.getWorld());
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            return false;
        }
        if (save) {
            try {
                handle.save(true, null);
                handle.saveLevel();
                WorldSaveEvent event = new WorldSaveEvent((World) handle.getWorld());
                Bukkit.getPluginManager().callEvent(event);
            } catch (ExceptionWorldConflict ex) {
                Bukkit.getLogger().log(Level.SEVERE, null, ex);
            }
        }
        worlds.remove(world.getName().toLowerCase());
        console.worlds.remove((Object) handle);
        //WorldUtil.unloadRegionFolder(world, null);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void unloadRegionFolder(World world, String region) {
        File parentFolder = world.getWorldFolder();
        Class<RegionFileCache> class_ = RegionFileCache.class;
        synchronized (RegionFileCache.class) {
            WorldUtil.removeFiles(parentFolder);
            Iterator i = RegionFileCache.a.entrySet().iterator();
            block5:
            while (i.hasNext()) {
                Map.Entry<File, RegionFile> entry = (Map.Entry<File, RegionFile>) i.next();
                for (File child = entry.getKey().getAbsoluteFile(); child != null; child = child.getParentFile()) {
                    if (!child.equals(parentFolder)) continue;
                    i.remove();
                    try {
                        RegionFileCloser.close(entry.getValue());
                    } catch (IOException ex) {
                        Bukkit.getLogger().log(Level.SEVERE, null, ex);
                    }
                    continue block5;
                }
            }
            return;
        }
    }

    private static void removeFiles(File file) {
        if (file.isDirectory()) {
            for (File fil : file.listFiles()) {
                WorldUtil.removeFiles(fil);
            }
        }
        //WorldUtil.removeFromCache(file);
    }

    public static void removeFromCache(File file) {
        RegionFile regionFile = (RegionFile) RegionFileCache.a.remove(file);
        if (regionFile == null && (regionFile = (RegionFile) RegionFileCache.a.remove(file.getAbsoluteFile())) != null) {
            Bukkit.getLogger().info("found match in absolute instead: " + file.getAbsoluteFile());
        }
        if (regionFile != null) {
            try {
                Bukkit.getLogger().info("Deleting cached regionFile: " + file.toString());
                RegionFileCloser.close(regionFile);
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }
}
