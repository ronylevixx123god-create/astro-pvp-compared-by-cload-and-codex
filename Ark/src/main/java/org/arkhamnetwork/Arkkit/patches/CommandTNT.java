/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.PlayerInventory
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

final class CommandTNT
        extends AbstractCommand {
    public CommandTNT(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!sender.hasPermission("autocrafttnt.command.tnt")) {
            sender.sendMessage(ChatColor.RED + "You do not have access to this command.");
            sender.sendMessage(ChatColor.GRAY + "Unlock this command and more by upgrading your rank today at " + Arkkit.get().getConfig().getString("language.spigot_branding.store_url") + "!");
            return true;
        }
        Player p = (Player) sender;
        PlayerInventory i = p.getInventory();
        ItemStack sand = new ItemStack(Material.SAND, 4);
        ItemStack sulphur = new ItemStack(Material.SULPHUR, 5);
        ItemStack tnt = new ItemStack(Material.TNT, 1);
        int c = 0;
        double moneySpent = 0.0;
        int sulphurUsed = 0;
        int sandUsed = 0;
        int tntToGive = 0;
        if ((i.containsAtLeast(sand, 4) || Arkkit.economy.has(p, AutoCraftTNT.sandPrice * 4.0)) && i.containsAtLeast(sulphur, 5)) {
            while ((i.containsAtLeast(sand, 4) || Arkkit.economy.has(p, moneySpent + AutoCraftTNT.sandPrice * 4.0)) && i.containsAtLeast(sulphur, 5)) {
                if (i.containsAtLeast(sand, 4)) {
                    i.removeItem(sand);
                    sandUsed += 4;
                } else {
                    moneySpent += AutoCraftTNT.sandPrice * 4.0;
                }
                i.removeItem(sulphur);
                sulphurUsed += 5;
                ++tntToGive;
                ++c;
            }
            Arkkit.economy.withdrawPlayer(p, moneySpent);
            p.updateInventory();
            if (sandUsed > 0) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- " + sandUsed + "x SAND");
            }
            if (sulphurUsed > 0) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- " + sulphurUsed + "x SULPHUR");
            }
            if (moneySpent > 0.0) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- $" + (int) moneySpent);
            }
            p.getInventory().addItem(new ItemStack(Material.TNT, tntToGive));
            p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.YELLOW + " You have auto-crafted " + c + "x TNT!");
            if (c > 0) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "contests add " + p.getName() + " tntCrafted " + c);
            }
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 0.75f, 2.3f);
        } else {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You do not have enough materials to create TNT.");
            p.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "REQ: " + ChatColor.GRAY + "5x gunpowder, 4x sand OR $" + AutoCraftTNT.sandPrice + "/sand");
        }
        return true;
    }
}

