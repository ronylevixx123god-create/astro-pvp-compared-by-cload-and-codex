/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.CosmicOutposts.CosmicOutpostsAPI
 *  com.wimbli.WorldBorder.WorldBorder
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.metadata.MetadataValue
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ExpUtils;
import com.wimbli.WorldBorder.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

final class ExpBottlesListener
        implements Listener {
    ExpBottlesListener() {
    }



    @EventHandler
    public void onExpBottleUse(PlayerInteractEvent e) {
        if (e.hasItem() && ExpBottles.isXpBottle(e.getItem())) {
            Player p = e.getPlayer();
            if (p.getWorld().getName().equals("world_duels")) {
                p.sendMessage(CC.translate("&c&l(!)&c You can only apply this in the world"));
                e.setCancelled(true);
                return;
            }
            int xpValue = ExpBottles.getXpAmount(e.getItem()) * e.getItem().getAmount();
            if (xpValue < 0)  {
                xpValue = Integer.MAX_VALUE;
            }
            e.setCancelled(true);
            //e.setUseInteractedBlock(Event.Result.DENY);
            //e.setUseItemInHand(Event.Result.DENY);
            p.setItemInHand(null);
            ExpUtils.giveXP(e.getPlayer(), xpValue);
            p.playSound(p.getLocation(), Sound.GLASS, 1.0f, 0.8f);
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "+ " + xpValue + " xp");
        }
    }
}

