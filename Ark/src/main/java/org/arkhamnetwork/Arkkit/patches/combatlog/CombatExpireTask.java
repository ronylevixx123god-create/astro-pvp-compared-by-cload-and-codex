/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.arkhamnetwork.Arkkit.patches.combatlog;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;

public class CombatExpireTask
        extends BukkitRunnable {
    public static final CopyOnWriteArrayList<WeakReference<Player>> combatTagged = new CopyOnWriteArrayList();

    public void run() {
        for (WeakReference<Player> p : combatTagged) {
            Player pl = p.get();
            if (pl == null || !pl.isValid()) {
                combatTagged.remove(p);
                continue;
            }
            if (CombatLog.inCombat(pl) || !pl.hasMetadata("cl_combat_monitor_task")) continue;
            combatTagged.remove(p);
            CombatLog.unflagCombat(pl);
        }
    }
}

