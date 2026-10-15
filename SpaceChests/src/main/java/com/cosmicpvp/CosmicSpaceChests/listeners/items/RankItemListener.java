/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicRankQuests.CosmicRankQuestsPlugin
 *  org.apache.commons.lang.StringUtils
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
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
 */
package com.cosmicpvp.CosmicSpaceChests.listeners.items;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.RankItems;
import org.apache.commons.lang.StringUtils;
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

public class RankItemListener
implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.hasItem() && e.getItem().getType() == Material.PAPER && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            String rankName = RankItems.getRankFromItemstack(e.getItem());
            if (rankName == null) {
                return;
            }
            if (e.getPlayer().getWorld().getName().equals("world_duels") || e.getPlayer().getWorld().getName().equals("world_duels2")) {
                e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You cannot redeem this item while in a duel.");
                return;
            }
            Player p = e.getPlayer();
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            if (e.getItem().getAmount() > 1) {
                p.sendMessage((Object)ChatColor.RED + "You cannot redeem stacked rank vouchers.");
                return;
            }
            String rankPerm = this.getRankPermCheck(rankName);
            if (p.hasPermission(rankPerm)) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "You already have the " + rankName + " rank (or higher) unlocked!");
                return;
            }
            try {
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("lp user " + p.getName() + " parent set " + rankName));
                p.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) You've UNLOCKED the " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + StringUtils.capitalise((String)rankName) + ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + " Rank!");
                p.sendMessage((Object)ChatColor.GRAY + "Congratulations! Enjoy the perks on your new rank!");
                if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                    ItemID.blacklistIID((long)ItemID.getItemID((ItemStack)p.getItemInHand()), (String)"rank item redeem");
                }
                p.setItemInHand(null);
                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 0.72f);
                Bukkit.getLogger().info("[RankUnlock] " + p.getName() + " (" + p.getUniqueId() + ") redeemed a " + rankName + " rank item!");
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    private String getRankPermCheck(String rank) {
        int id = RankItems.getRankID(rank);
        if (id == -1) {
            return null;
        }
        return id == 5 ? "enchantments.max.20" : "enchantments.max." + id * 3;
    }
}

