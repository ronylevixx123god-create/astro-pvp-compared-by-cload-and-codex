/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches.combatlog;

import org.bukkit.scheduler.BukkitRunnable;

final class PlayerLogoutMonitorTask
        extends BukkitRunnable {
    public int logouts = 0;

    PlayerLogoutMonitorTask() {
    }

    public void run() {
        this.logouts = 0;
    }
}

