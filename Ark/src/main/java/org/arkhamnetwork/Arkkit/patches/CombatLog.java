/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.entity.Player;

public class CombatLog {
    @Deprecated
    public static boolean inCombat(Player pl) {
        return org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog.inCombat(pl);
    }

    @Deprecated
    public static long getTimeLeftInCombat(Player pl) {
        return org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog.getTimeLeftInCombat(pl);
    }

    @Deprecated
    public static void flagCombat(Player pl) {
        org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog.flagCombat(pl);
    }

    @Deprecated
    public static void unflagCombat(Player pl) {
        org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog.unflagCombat(pl);
    }
}

