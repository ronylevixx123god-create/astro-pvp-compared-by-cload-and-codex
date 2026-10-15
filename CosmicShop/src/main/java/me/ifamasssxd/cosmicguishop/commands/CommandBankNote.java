/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicguishop.commands;

import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import me.ifamasssxd.cosmicguishop.CosmicGUIShop;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBankNote implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage((Object)ChatColor.RED + "/withdraw <$>");
            sender.sendMessage((Object)ChatColor.GRAY + "This will create a cosmic note with the equivalent value of <$>.");
            sender.sendMessage((Object)ChatColor.GRAY + "You can use keywords 'million', 'billion', 'trillion' for withdrawls.");
            return true;
        }
        if (args[0].contains(".")) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot sign decimal amounts on cosmic notes.");
            return true;
        }
        long withdraw_val = 0L;
        try {
            withdraw_val = ArkhamMoneyNotesAPI.getMoneyValue(args[0]);
        }
        catch (Exception err) {
            sender.sendMessage((Object)ChatColor.RED + "You specified an invalid withdrawl amount.");
            return true;
        }
        if ((double)withdraw_val > 1.0E15) {
            sender.sendMessage((Object)ChatColor.RED + "You cannot withdraw more than 1 Quadrillion in a single cosmic note!");
            return true;
        }
        Player pl = (Player)sender;
        if (withdraw_val <= 0L) {
            pl.sendMessage((Object)ChatColor.RED + "You must /withdraw atleast $1 at a time.");
            return true;
        }
        if (!CosmicGUIShop.getEconomy().has((OfflinePlayer)pl, Double.valueOf(withdraw_val).doubleValue())) {
            pl.sendMessage((Object)ChatColor.RED + "You do not have enough $ to sign a cosmic note that large!");
            pl.performCommand("balance");
            return true;
        }
        if (pl.getInventory().firstEmpty() == -1) {
            pl.sendMessage((Object)ChatColor.RED + "You do not have enough room in your inventory.");
            return true;
        }
        CosmicGUIShop.getEconomy().withdrawPlayer((OfflinePlayer)pl, (double)withdraw_val);
        pl.sendMessage(String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD.toString() + "- $" + withdraw_val);
        ItemStack moneyNote = ArkhamMoneyNotesAPI.getMoneyNote(pl.getName(), withdraw_val);
        pl.getInventory().addItem(new ItemStack[]{moneyNote});
        pl.sendMessage((Object)ChatColor.GREEN + "You have signed a cosmic note for $" + withdraw_val + "!");
        pl.playSound(pl.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.25f);
        return true;
    }

    private boolean isBankerNearby(Player pl) {
        for (Entity ent : pl.getNearbyEntities(32.0, 32.0, 32.0)) {
            if (!(ent instanceof Player) || !ent.hasMetadata("NPC") || !((Player)ent).getName().contains("Banker")) continue;
            return true;
        }
        return false;
    }
}

