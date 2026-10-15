/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.CosmicCustomTNT.cmds;

import org.arkhamnetwork.CosmicCustomTNT.enums.TNTTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandCTNT
implements CommandExecutor {
    String tntTypes = "";

    public CommandCTNT() {
        for (TNTTypes tnt : TNTTypes.values()) {
            this.tntTypes = this.tntTypes + tnt.name() + ", ";
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage((Object)ChatColor.YELLOW + "/ctnt <playerName> <tntType> <amount>");
            sender.sendMessage((Object)ChatColor.GRAY + "tntTypes: " + this.tntTypes);
            return true;
        }
        String playerName = args[0];
        TNTTypes tntType = TNTTypes.valueOf(args[1]);
        int amount = Integer.parseInt(args[2]);
        Player p = Bukkit.getPlayer((String)playerName);
        if (p != null) {
            ItemStack i = TNTTypes.getTNTItem(tntType).clone();
            i.setAmount(amount);
            p.getInventory().addItem(new ItemStack[]{i});
            sender.sendMessage((Object)ChatColor.GRAY + "Gave " + amount + "x " + tntType.name() + " TNT to " + p.getName());
        } else {
            sender.sendMessage((Object)ChatColor.RED + "Cannot find player " + playerName);
        }
        return true;
    }
}

