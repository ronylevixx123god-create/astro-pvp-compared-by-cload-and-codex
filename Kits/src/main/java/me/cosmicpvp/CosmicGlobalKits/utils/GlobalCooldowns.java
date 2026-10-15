/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.cosmicpvp.CosmicGlobalKits.utils;

import java.util.concurrent.TimeUnit;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.database.SQLManager;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class GlobalCooldowns {
    public static boolean hasCooldown(Player pl, GlobalKits gkit) {
        if (pl.hasMetadata("CosmicGlobalKits_kit_" + gkit.getKitName())) {
            return System.currentTimeMillis() - ((MetadataValue)pl.getMetadata("CosmicGlobalKits_kit_" + gkit.getKitName()).get(0)).asLong() < gkit.getCooldown(pl);
        }
        return false;
    }

    public static long getCooldownMS(Player pl, GlobalKits gkit) {
        return pl.hasMetadata("CosmicGlobalKits_kit_" + gkit.getKitName()) ? gkit.getCooldown(pl) - (System.currentTimeMillis() - ((MetadataValue)pl.getMetadata("CosmicGlobalKits_kit_" + gkit.getKitName()).get(0)).asLong()) : 0L;
    }

    public static void setLastKitUsed(Player pl, GlobalKits gkit, long last_use) {
        pl.setMetadata("CosmicGlobalKits_kit_" + gkit.getKitName(), (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)last_use));
    }

    public static void setCooldown(Player pl, GlobalKits gkit, long last_use) {
        GlobalCooldowns.setLastKitUsed(pl, gkit, last_use);
    }

    public static void setGlobalKitTier(Player pl, GlobalKits gkit, int newTier) {
        SQLManager.sql_query.add(String.format("INSERT IGNORE INTO globalkit_player_tiers(player_uuid, player_name, global_kit, tier, server_name) VALUES ('%s', '%s', '%s',  %s, '%s') ON DUPLICATE KEY UPDATE tier = %s;", pl.getUniqueId().toString(), pl.getName(), gkit.name(), newTier, CosmicGlobalKits.getPlugin().getServerName(), newTier));
        Bukkit.getLogger().info("[GlobalKits] Setting " + pl.getName() + "'s " + gkit.name() + " gkit to tier " + newTier + " in db!");
    }

    public static void startCooldown(Player pl, GlobalKits gkit) {
        int tier = CosmicGlobalKitsAPI.getKitTier(pl, gkit);
        pl.setMetadata("CosmicGlobalKits_kit_" + gkit.getKitName(), (MetadataValue)new FixedMetadataValue((Plugin)CosmicGlobalKits.getPlugin(), (Object)System.currentTimeMillis()));
        SQLManager.sql_query.add("INSERT INTO " + CosmicGlobalKits.sql_table + "(player_uuid, player_name, global_kit, last_use) VALUES('" + pl.getUniqueId().toString() + "', '" + pl.getName() + "', '" + gkit.name() + "', " + System.currentTimeMillis() + ") ON DUPLICATE KEY UPDATE last_use=" + System.currentTimeMillis() + ";");
        CosmicGlobalKitsAPI.registerGkitLevels(pl, gkit);
    }

    public static void clearCooldowns(Player pl) {
        for (GlobalKits gkit : GlobalKits.values()) {
            pl.removeMetadata("CosmicGlobalKits_kit_" + gkit.getKitName(), (Plugin)CosmicGlobalKits.getPlugin());
        }
    }

    public static void removeCooldown(Player pl, GlobalKits gkit) {
        pl.removeMetadata("CosmicGlobalKits_kit_" + gkit.getKitName(), (Plugin)CosmicGlobalKits.getPlugin());
    }

    public static String getCooldownString(int seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (long)(day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        return (day > 0 ? day + "d " : "") + hours + "h " + minute + "m " + second + "s";
    }
}

