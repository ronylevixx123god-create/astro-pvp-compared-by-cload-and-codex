/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.ArkPluginListener;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.metadata.FixedMetadataValue;

final class ClearInventoryListener
        extends ArkPluginListener<ClearInventoryConfirm> {
    public ClearInventoryListener(ClearInventoryConfirm patch) {
        super(patch);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (p.hasMetadata("compilex_clearinventory_pending")) {
            e.setCancelled(true);
            String msg = ChatColor.stripColor(e.getMessage());
            String cmd = p.getMetadata("compilex_clearinventory_pending").get(0).asString().replace("/", "");
            if (msg.equalsIgnoreCase("no")) {
                p.sendMessage(ChatColor.RED + "Inventory deletion cancelled.");
                p.removeMetadata("compilex_clearinventory_pending", Arkkit.get());
            } else if (msg.equalsIgnoreCase("yes")) {
                p.setMetadata("compilex_clearinventory_confirmed", new FixedMetadataValue(Arkkit.get(), "/" + cmd));
                p.removeMetadata("compilex_clearinventory_pending", Arkkit.get());
                if (!CombatLog.inCombat(p)) {
                    p.performCommand(cmd);
                } else {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot clear your inventory while in combat!");
                }
            } else {
                p.sendMessage(ChatColor.RED + "Invalid /clearinventory response.");
                p.sendMessage(ChatColor.GRAY + "Please type " + ChatColor.GREEN + ChatColor.BOLD + "YES" + ChatColor.GRAY + " or " + ChatColor.RED + ChatColor.BOLD + "NO" + ChatColor.GRAY + " to confirm " + ChatColor.UNDERLINE + "deleting your inventory.");
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        String rootCommand = e.getMessage().toLowerCase();
        if (rootCommand.contains(" ")) {
            rootCommand = rootCommand.split(" ")[0];
        }
        if (rootCommand.equals("/ci") || rootCommand.equals("/clearinventory") || rootCommand.equals("/eci") || rootCommand.equals("/clean") || rootCommand.equals("/eclean") || rootCommand.equals("/clear") || rootCommand.equals("/eclear") || rootCommand.equals("/clearinvent") || rootCommand.equals("/eclearinvent") || rootCommand.equals("/eclearinventory")) {
            Player p = e.getPlayer();
            if (!p.hasPermission("essentials.clearinventory")) {
                return;
            }
            if (p.hasPermission("essentials.clearinventory.noconfirm")) {
                return;
            }
            String cmd = e.getMessage();
            if (!(this.patch.getClearInventoryToggled().contains(p.getUniqueId()) || p.hasMetadata("compilex_clearinventory_confirmed") && p.getMetadata("compilex_clearinventory_confirmed").get(0).asString().equals(cmd))) {
                e.setCancelled(true);
                e.setMessage("/null");
                p.setMetadata("compilex_clearinventory_pending", new FixedMetadataValue(Arkkit.get(), cmd));
                p.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "Are you sure you want to delete items in your inventory?");
                p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Command: " + ChatColor.GRAY + cmd);
                p.sendMessage(ChatColor.YELLOW + "** " + ChatColor.GRAY + "Please type " + ChatColor.GREEN + ChatColor.BOLD + ChatColor.UNDERLINE + "YES" + ChatColor.GRAY + " or " + ChatColor.RED + ChatColor.BOLD + ChatColor.UNDERLINE + "NO");
            } else {
                p.removeMetadata("compilex_clearinventory_confirmed", Arkkit.get());
            }
        }
    }
}

