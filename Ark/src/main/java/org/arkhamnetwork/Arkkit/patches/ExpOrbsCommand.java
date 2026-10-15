/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.libs.ExpUtils;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

final class ExpOrbsCommand
        extends AbstractCommand {
    public ExpOrbsCommand(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p;
        Player player = p = sender instanceof Player ? (Player) sender : null;
        if (p == null) {
            return false;
        }
        if (args.length != 1) {
            p.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "/xporb <amount>");
            p.sendMessage(ChatColor.GRAY + "Converts <amount> xp into item form.");
            p.sendMessage(ChatColor.GRAY + "Use " + ChatColor.GOLD + "/xp" + ChatColor.GRAY + " to display your exp points.");
            return true;
        }
        int amount = 0;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            p.sendMessage(ChatColor.RED + "Invalid <amount> specified.");
            return true;
        }
        if (amount < 100) {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You must withdraw at least 100XP points per orb.");
            return true;
        }
        int currentXp = p.getTotalExperience();
        if (currentXp < amount) {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You only have " + currentXp + " xp points to convert.");
            p.sendMessage(ChatColor.GRAY + "View your exp values with /xp");
            return true;
        }
        if (p.getInventory().firstEmpty() == -1) {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You do not have any inventory space.");
            return true;
        }
        FLocation pLoc = new FLocation(p.getLocation());
        if (!p.isOp() && FactionUtils.getFactionAt(pLoc).isWarZone() && !WorldGuardUtils.getRegionName(p.getLocation()).startsWith("spawn_")) {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " convert EXP in Warzones.");
            return true;
        }
        int exp = (player.getTotalExperience() - amount);
        p.setTotalExperience(0);
        p.setExp(0);
        p.setLevel(0);
        p.giveExp(exp);

        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- " + amount + " xp");
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.2f);
        p.getInventory().addItem(ExpOrbs.getXpOrb(amount, p.getName()));
        return true;
    }
}

