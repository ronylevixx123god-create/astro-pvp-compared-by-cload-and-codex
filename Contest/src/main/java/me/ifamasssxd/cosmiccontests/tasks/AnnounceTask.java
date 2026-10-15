/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmiccontests.tasks;

import java.util.concurrent.TimeUnit;
import me.ifamasssxd.cosmiccontests.CosmicContests;
import me.ifamasssxd.cosmiccontests.struct.ContestManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AnnounceTask
implements Runnable {
    @Override
    public void run() {
        ContestManager manager = CosmicContests.get().getContestManager();
        if (System.currentTimeMillis() - manager.getLastReset() <= TimeUnit.HOURS.toMillis(1L)) {
            String msg = "      " + (Object)ChatColor.GREEN + (Object)ChatColor.BOLD + "** NEW CHALLENGES HAVE STARTED! **";
            String msg2 = "            " + (Object)ChatColor.GRAY + "Use '/challenge' to view rankings!";
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.sendMessage(msg);
                pl.sendMessage(msg2);
            }
        }
    }
}

