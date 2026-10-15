package me.kingsavaqe.cagepvp.combattag;

import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.entity.Player;

public class CombatTagManager {
    public boolean isTagged(Player player) {
        return CombatLog.inCombat(player);
    }

    public void removeTag(Player player) {
        CombatLog.unflagCombat(player);
    }
}
