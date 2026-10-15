/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLogCombatListener;
import org.bukkit.entity.Player;

final class CombatListener {
    CombatListener() {
    }

    @Deprecated
    public static int getTimeLeftMSInCombat(Player pl) {
        return CombatLogCombatListener.getTimeLeftMSInCombat(pl);
    }
}

