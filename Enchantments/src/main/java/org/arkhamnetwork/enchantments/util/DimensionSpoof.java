package org.arkhamnetwork.enchantments.util;

import net.minecraft.server.v1_8_R3.*;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class DimensionSpoof {
    public static void setPlayerEnvironment(final Player pl, final World.Environment env) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
            @Override
            public void run() {
                int dimension = 0;
                if (env == World.Environment.NORMAL) {
                    dimension = 0;
                }
                if (env == World.Environment.NETHER) {
                    dimension = -1;
                }
                if (env == World.Environment.THE_END) {
                    dimension = 1;
                }
                if (pl == null) {
                    return;
                }
                final boolean fly = pl.isFlying();
                final double hp = pl.getHealth();
                final CraftPlayer cp = (CraftPlayer) pl;
                final EntityPlayer ep = cp.getHandle();
                final WorldServer worldserver = ((CraftWorld) pl.getWorld()).getHandle();
                final PacketPlayOutRespawn packet_a = new PacketPlayOutRespawn((byte) dimension, worldserver.getDifficulty(), worldserver.getWorldData().getType(), ep.playerInteractManager.getGameMode());
                cp.getHandle().playerConnection.sendPacket(packet_a);
                final Chunk c = pl.getLocation().getChunk();
                for (int x = -6; x < 6; ++x) {
                    for (int z = -6; z < 6; ++z) {
                        pl.getWorld().refreshChunk(c.getX() + x, c.getZ() + z);
                    }
                }
                pl.setFlying(fly);
                //pl.setHealth(hp);
                Enchantments.getInstance().getServer().getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
                    @Override
                    public void run() {
                        updatePlayerView(pl);
                        updateEntities(pl);
                    }
                }, 2L);
            }
        });
    }

    private static void updatePlayerView(final Player player) {
        if (player == null || ((CraftPlayer) player).getHandle().activeContainer == null) {
            return;
        }
        ((CraftPlayer) player).getHandle().updateInventory(((CraftPlayer) player).getHandle().activeContainer);
    }

    private static void updateEntities(final Player player) {
        final WorldServer ws = ((CraftWorld) player.getWorld()).getHandle();
        final EntityTracker tracker = ws.tracker;
        for (final Entity ent : player.getNearbyEntities(32.0, 32.0, 32.0)) {
            final EntityTrackerEntry entry = tracker.trackedEntities.get(ent.getEntityId());
            final List nms = new ArrayList();
            nms.add(((CraftPlayer) player).getHandle());
            entry.trackedPlayers.removeAll(nms);
            entry.scanPlayers(nms);
        }
    }
}
