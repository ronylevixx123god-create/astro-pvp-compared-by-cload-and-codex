/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.WorldGuardUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.World$Environment
 *  org.bukkit.entity.Player
 */
package me.ifamasssxd.cosmiccontests.tasks;

import me.ifamasssxd.cosmiccontests.struct.contests.Contests;
import me.ifamasssxd.cosmiccontests.struct.user.ContestUser;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TimeSpentTask
implements Runnable {
    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            if (world.getEnvironment() != World.Environment.THE_END) continue;
            for (Player pl : world.getPlayers()) {
                if (pl.hasMetadata("NPC") || WorldGuardUtils.isPvPDisabled((Location)pl.getLocation())) continue;
                ContestUser user = ContestUser.getUser(pl.getUniqueId());
                user.incrementIntAmount(Contests.END_TIME_SPENT, "endTimeSpent", 1);
            }
        }
    }
}

