/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.event.LandClaimEvent
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 */
package org.arkhamnetwork.Arkkit.patches;

import com.massivecraft.factions.event.LandClaimEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

final class DisableClaimsListener
        implements Listener {
    protected static boolean noClaim = false;

    DisableClaimsListener() {
    }

    @EventHandler
    public void onFactionClaimLand(LandClaimEvent e) {
        if (noClaim && !e.getPlayer().isOp()) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "Land claiming has been disabled by an admin.");
        }
    }
}

