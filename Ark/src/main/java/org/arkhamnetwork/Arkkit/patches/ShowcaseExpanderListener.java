/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.hanging.HangingPlaceEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

final class ShowcaseExpanderListener
        implements Listener {
    ShowcaseExpanderListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerUseItem(PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && ShowcaseExpander.isShowcaseExpanderItem(e.getItem()) && !e.getPlayer().getWorld().getName().contains("duels")) {
            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
            Player p = e.getPlayer();
            ItemStack i = e.getItem();
            if (i.getAmount() > 1) {
                i.setAmount(i.getAmount() - 1);
                p.setItemInHand(i);
            } else {
                p.setItemInHand(null);
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "editshowcase " + p.getName() + " add 1");
            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + " +9 /showcase Slots!");
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.7f, 1.0f);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerPlacePainting(HangingPlaceEvent e) {
        if (ShowcaseExpander.isShowcaseExpanderItem(e.getPlayer().getItemInHand())) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            ItemStack i = e.getPlayer().getItemInHand();
            if (i.getAmount() > 1) {
                i.setAmount(i.getAmount() - 1);
                p.setItemInHand(i);
            } else {
                p.setItemInHand(null);
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "editshowcase " + p.getName() + " add 1");
            p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + " +9 /showcase Slots!");
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.7f, 1.0f);
        }
    }
}

