/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.recolance.auction.tasks;

import me.recolance.auction.auction.UnclaimedListing;
import me.recolance.auction.data.DataHolder;
import me.recolance.auction.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ItemExpireWarnTask
        extends BukkitRunnable {
    public void run() {
        block0:
        for (Player p : Bukkit.getOnlinePlayers()) {
            List<UnclaimedListing> listings = DataHolder.getUnclaimedListings().get(p.getUniqueId());
            if (listings == null || listings.size() <= 0) continue;
            for (UnclaimedListing ul : listings) {
                long l = ul.getExpireTime() - System.currentTimeMillis();
                if (l <= 0L || l > 900000L) continue;
                p.sendMessage((Object) ChatColor.RED + "" + (Object) ChatColor.BOLD + "(!) " + (Object) ChatColor.RED + "You have item(s) in your " + (Object) ChatColor.UNDERLINE + "/ah expired" + (Object) ChatColor.RED + " that are going to be deleted in " + (Object) ChatColor.BOLD + Util.timeToDHMSString(l));
                p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.2f, 1.25f);
                continue block0;
            }
        }
    }
}

