/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.enchantments.enchantments.armor;

import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

final class reducePhoenixCount
        extends BukkitRunnable {
    reducePhoenixCount() {
    }

    @Override
    public void run() {
        Bukkit.getLogger().info("[Enchantments (Phoenix)] Reducing all phoenixProcs by 1.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasMetadata("phoenixProcs")) continue;
            player.setMetadata("phoenixProcs", new FixedMetadataValue((Plugin)((Object)Enchantments.getInstance()), Math.max(0, player.getMetadata("phoenixProcs").get(0).asInt() - 1)));
        }
    }
}
