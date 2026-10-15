/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import com.google.common.collect.Lists;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.patches.balancetop.CommandBalanceTop;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

final class ExpCombineCommand
        extends AbstractCommand {
    public ExpCombineCommand(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        int totalExpFound = 0;
        ArrayList<ItemStack> bottles = Lists.newArrayList();
        int bottlesFound = 0;
        Player player = (Player) sender;
        for (ItemStack item2 : player.getInventory()) {
            if (!ExpBottles.isXpBottle(item2)) continue;
            totalExpFound += ExpBottles.getXpAmount(item2) * item2.getAmount();
            bottles.add(item2);
            bottlesFound += item2.getAmount();
        }
        if (bottlesFound > 1 && totalExpFound > 0) {
            bottles.forEach(item -> player.getInventory().removeItem(item));
            player.getInventory().addItem(ExpBottles.getXpBottle(totalExpFound, player.getName()));
            player.updateInventory();
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0f, 1.4f);
            player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "Combined " + ChatColor.GREEN + ChatColor.UNDERLINE + bottlesFound + ChatColor.GREEN + " EXP Bottles into a " + ChatColor.GREEN + ChatColor.BOLD.toString() + CommandBalanceTop.insertCommas(totalExpFound) + ChatColor.GREEN + " EXP Bottle!");
        } else {
            sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You do not have any EXP bottles to combine!");
        }
        return false;
    }
}

