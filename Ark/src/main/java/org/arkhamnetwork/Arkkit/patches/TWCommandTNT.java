/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

final class TWCommandTNT
        extends AbstractCommand {
    public TWCommandTNT(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        boolean vip = p.hasPermission("cosmicpvp.vip");
        PlayerInventory i = p.getInventory();
        ItemStack sand = new ItemStack(Material.SAND, 4);
        ItemStack sulphur = new ItemStack(Material.SULPHUR, 5);
        ItemStack tnt = new ItemStack(Material.TNT, 1);
        int c = 0;
        double moneySpent = 0.0;
        int sulphurUsed = 0;
        int sandUsed = 0;
        int tntToGive = 0;
        if ((i.containsAtLeast(sand, 4) || vip && Arkkit.economy.has(p, TWAutoCraftTNT.sandPrice * 4.0)) && i.containsAtLeast(sulphur, 5)) {
            while ((i.containsAtLeast(sand, 4) || vip && Arkkit.economy.has(p, TWAutoCraftTNT.sandPrice * 4.0)) && i.containsAtLeast(sulphur, 5)) {
                if (i.containsAtLeast(sand, 4)) {
                    i.removeItem(sand);
                    sandUsed += 4;
                } else if (vip) {
                    Arkkit.economy.withdrawPlayer(p, TWAutoCraftTNT.sandPrice * 4.0);
                    moneySpent += TWAutoCraftTNT.sandPrice * 4.0;
                }
                i.removeItem(sulphur);
                sulphurUsed += 5;
                ++tntToGive;
                ++c;
            }
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
            p.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.YELLOW + " You crafted " + c + "x TNT!");
            if (!vip) {
                p.sendMessage(ChatColor.GRAY + "Unlock the ability to use /tnt without sand blocks by purchasing a rank today at " + Arkkit.get().getConfig().getString("language.spigot_branding.store_url") + "!");
            }
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1.2f, 2.3f);
        } else {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You do not have the materials to craft TNT.");
            p.sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "REQ: " + ChatColor.GRAY + "5X Sulphur, 4X Sand OR $" + TWAutoCraftTNT.sandPrice + "/sand if ranked.");
        }
        return true;
    }
}

