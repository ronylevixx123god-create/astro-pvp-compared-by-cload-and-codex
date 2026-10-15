/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

final class NoCombatFlyingTask
        extends BukkitRunnable {
    NoCombatFlyingTask() {
    }

    public void run() {
        for (Player pl : Arkkit.get().getServer().getOnlinePlayers()) {
            try {
                if (!NoCombatFlying.inCombat(pl.getName()) || !pl.isFlying() && !pl.getAllowFlight()) continue;
                pl.setAllowFlight(false);
                pl.setFlying(false);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}

