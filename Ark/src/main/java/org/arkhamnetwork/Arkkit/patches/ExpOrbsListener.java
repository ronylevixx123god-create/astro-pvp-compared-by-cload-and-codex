/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.ExpUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

final class ExpOrbsListener
        implements Listener {
    ExpOrbsListener() {
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onExpOrbUse(PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && ExpOrbs.isXpBottle(e.getItem())) {
            Player p = e.getPlayer();
            int xpValue = ExpOrbs.getXpAmount(e.getItem()) * e.getItem().getAmount();
            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
            p.setItemInHand(null);
            int exp = (int) (p.getTotalExperience() + xpValue);
            p.setTotalExperience(0);
            p.setExp(0);
            p.setLevel(0);
            p.giveExp(exp);
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ " + xpValue + " xp");
        }
    }
}

