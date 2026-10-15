/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.ArkPluginCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandToggleClearInventoryConfirm
        extends ArkPluginCommand<ClearInventoryConfirm> {
    CommandToggleClearInventoryConfirm(ClearInventoryConfirm patch) {
        super(patch, "toggleciconfirm", "/<command>", "Clear inventory confirm.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player pl = (Player) sender;
        if (this.patch.getClearInventoryToggled().contains(pl.getUniqueId())) {
            this.patch.getClearInventoryToggled().remove(pl.getUniqueId());
            pl.sendMessage(ChatColor.YELLOW + "/ci confirmation has been " + ChatColor.GREEN + ChatColor.BOLD + "ENABLED");
        } else {
            this.patch.getClearInventoryToggled().add(pl.getUniqueId());
            pl.sendMessage(ChatColor.YELLOW + "/ci confirmation has been " + ChatColor.RED + ChatColor.BOLD + "DISABLED");
        }
        return false;
    }
}

