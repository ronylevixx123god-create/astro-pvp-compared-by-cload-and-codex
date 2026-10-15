/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.CosmicOutposts.CosmicOutpostsAPI
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.libs.ExpUtils;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;
import java.util.concurrent.TimeUnit;

final class ExpBottlesCommand
        extends AbstractCommand {
    public ExpBottlesCommand(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }



    protected static String getTimeString(long millis) {
        return String.format("%02dm %02ds", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p;
        Player player = p = sender instanceof Player ? (Player) sender : null;
        if (p == null) {
            return false;
        }
        if (args.length != 1) {
            p.sendMessage(ChatColor.YELLOW + "/xpbottle <amount>");
            p.sendMessage(ChatColor.GRAY + "Converts <amount> xp into bottle form.");
            p.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/xp" + ChatColor.GRAY + " to view your current exp points.");
            return true;
        }
        int amount = 0;
        if (args[0].equalsIgnoreCase("all")) {
            amount = p.getTotalExperience();
        } else {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                p.sendMessage(ChatColor.RED + "Invalid <amount> specified.");
                return true;
            }
        }
        if (amount < 25) {
            p.sendMessage(ChatColor.RED + "You must withdraw at least 25XP points per bottle.");
            return true;
        }
        int currentXp = p.getTotalExperience();
        if (currentXp < amount) {
            p.sendMessage(ChatColor.RED + "You only have " + currentXp + " xp points to convert.");
            p.sendMessage(ChatColor.GRAY + "View your exp values with /xp");
            return true;
        }
        if (p.getInventory().firstEmpty() == -1) {
            p.sendMessage(ChatColor.RED + "You do not have any inventory space.");
            return true;
        }
        int exp = player.getTotalExperience() - amount;
        player.setTotalExperience(0);
        player.setExp(0);
        player.setLevel(0);
        player.giveExp(exp);
        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "- " + amount + " xp");
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.2f);
        p.getInventory().addItem(ExpBottles.getXpBottle((int) amount, p.getName()));
        return true;
    }

}

