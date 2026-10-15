/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.arkhamnetwork.permissions.ArkhamPermissions
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package com.cosmicpvp.CosmicSpaceChests.listeners.items;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.TitleItems;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class TitleItemListener
implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().getType() == Material.PAPER && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            String title = TitleItems.getTitleFromItemstack(e.getItem());
            if (title == null) {
                return;
            }
            if (e.getPlayer().getWorld().getName().equals("world_duels") || e.getPlayer().getWorld().getName().equals("world_duels2")) {
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You cannot redeem this item while in a duel.");
                return;
            }
            String titlePerm = "titles.title." + title;
            final Player p = e.getPlayer();
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            if (e.getItem().getAmount() > 1) {
                p.sendMessage((Object)ChatColor.RED + "You cannot redeem stacked title vouchers.");
                return;
            }
            if (p.hasPermission(titlePerm)) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You already have the " + title + " title unlocked!");
                p.sendMessage((Object)ChatColor.GRAY + "Use /title to equip it!");
                return;
            }
            try {
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("lp user " + p.getName() + " permission set " + titlePerm + " true"));
                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) You've UNLOCKED the " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + title + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + " title!");
                p.sendMessage((Object)ChatColor.GRAY + "Equip your new title in the /title menu!");
                if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                    ItemID.blacklistIID((long)ItemID.getItemID((ItemStack)p.getItemInHand()), (String)"title item redeem");
                }
                p.setItemInHand(null);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 0.72f);

            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}

