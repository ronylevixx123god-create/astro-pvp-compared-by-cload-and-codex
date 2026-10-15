/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.confuser.barapi.BarAPI
 *  org.arkhamnetwork.Arkkit.Arkkit
 *  org.arkhamnetwork.Arkkit.patches.CombatLog
 */
package com.cosmicpvp.cosmicutils.utils;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.compat.ASound;
import me.confuser.barapi.BarAPI;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerUtil {
    static Method method;

    static {
        try {
            method = ReflectionUtils.getMethod(Bukkit.getServer().getClass(), "getOnlinePlayers");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Player getPlayerFromString(String name) {
        if (name.length() > 16 && name.contains("-")) {
            try {
                UUID uuid = UUID.fromString(name);
                return Bukkit.getPlayer((UUID) uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Bukkit.getPlayer(name);
    }

    public static Collection<? extends Player> getOnlinePlayers() {
        try {
            Object retr = method.invoke(Bukkit.getServer(), new Object[0]);
            if (retr instanceof Player[]) {
                Player[] players = (Player[]) retr;
                return Arrays.stream(players).collect(Collectors.toList());
            }
            return (Collection) retr;
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().info("Unable to retreive online players from getOnlinePlayers reflection call!");
            return new HashSet();
        }
    }

    public static boolean giveOrDropProtectedItem(Player player, ItemStack item, int protectionTimer, String addedMessage, String floorMessage) {
        boolean added = PlayerUtil.giveOrDropProtectedItem(player, item, protectionTimer);
        if (added && addedMessage != null) {
            player.sendMessage(addedMessage);
        }
        if (!added && floorMessage != null) {
            player.sendMessage(floorMessage);
        }
        return added;
    }

    public static boolean giveOrDropProtectedItem(Player player, ItemStack item, int protectionTimer) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(item);
            return true;
        }
        Item itemEntity = player.getWorld().dropItem(player.getLocation(), item);
        itemEntity.setMetadata("protectedItem", new FixedMetadataValue((Plugin) CosmicUtils.get(), protectionTimer == -1 ? (long) protectionTimer : System.currentTimeMillis() + (long) (protectionTimer * 1000)));
        itemEntity.setMetadata("protectedOwner", new FixedMetadataValue((Plugin) CosmicUtils.get(), player.getUniqueId().toString()));
        ASound.CHICKEN_EGG_POP.play(player, 1.1f);
        return false;
    }

    public static int getEmptyInventorySlots(Player pl) {
        int slots = 0;
        for (int i = 0; i < pl.getInventory().getSize(); ++i) {
            ItemStack item = pl.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) continue;
            ++slots;
        }
        return slots;
    }

    public static boolean unflagCombat(Player player) {
        if (!CosmicUtils.get().isArkkitEnabled()) {
            return false;
        }
        CombatLog.unflagCombat((Player) player);
        player.removeMetadata("cl_combat", (Plugin) Arkkit.get());
        return true;
    }

    public static void removeBossBar(Player player) {
        BarAPI.removeBar((Player) player);
    }

    public static void sendBossBar(Player player, String msg, float percentage) {
        BarAPI.setMessage(player, (String) msg, (float) percentage);
    }

    public static void sendBossBar(Player player, String msg) {
        PlayerUtil.sendBossBar(player, msg, 100.0f);
    }

    public static void sendBossBar(Player player, String msg, int seconds) {
        BarAPI.setMessage((Player) player, (String) msg, (int) seconds);
    }
}

