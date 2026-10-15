/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.scheduler.BukkitRunnable;

final class AntiTileEntityLagTask
        extends BukkitRunnable {
    AntiTileEntityLagTask() {
    }

    public void run() {
        AntiTileEntityLag.purgeBrewingStands();
    }
}

