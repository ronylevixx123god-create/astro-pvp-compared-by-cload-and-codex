/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package org.arkhamnetwork.Arkkit.patches;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

final class CommandRoll
        extends AbstractCommand {
    private final HashMap<String, Long> roll_delay = new HashMap();

    public CommandRoll(String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            int roll;
            Player p = (Player) sender;
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect Syntax. " + ChatColor.GRAY + "/roll <1-10000/toggle>");
                p.sendMessage(ChatColor.GRAY + "Rolls a random number between 0 and X and outputs the result to local chat.");
                return true;
            }
            if (args[0].length() > 5) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect Syntax. " + ChatColor.GRAY + "/roll <1-10000/toggle>");
                return true;
            }
            String number = args[0];
            ThreadLocalRandom dice = ThreadLocalRandom.current();
            if (number.equalsIgnoreCase("stfu") || number.equalsIgnoreCase("toggle")) {
                if (Gamble.disabledRollUUIDs.contains(p.getUniqueId().toString())) {
                    Gamble.disabledRollUUIDs.remove(p.getUniqueId().toString());
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "(!) " + ChatColor.GREEN + "You have enabled displaying /roll numbers!");
                } else {
                    Gamble.disabledRollUUIDs.add(p.getUniqueId().toString());
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + "You have disabled displaying /roll numbers!");
                }
                return true;
            }
            if (Gamble.disabledRollUUIDs.contains(p.getUniqueId().toString())) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot use /roll while having it silenced. Use " + ChatColor.UNDERLINE + "/roll toggle" + ChatColor.RED + " to re-enable.");
                return true;
            }
            if (number.contains("-")) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Non-Positive Max Number. " + ChatColor.RED + "/roll <1-10000>");
                return true;
            }
            if (number.startsWith("0")) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect Syntax. " + ChatColor.GRAY + "/roll <1-10000>");
                return true;
            }
            Long time = this.roll_delay.get(p.getName());
            if (time != null && System.currentTimeMillis() - time <= 4000L) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You need to wait longer between uses of /roll!");
                return true;
            }
            this.roll_delay.put(p.getName(), System.currentTimeMillis());
            try {
                roll = ((Random) dice).nextInt(Integer.parseInt(number) + 1);
            } catch (NumberFormatException nfe) {
                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Non-Numeric Max Number. " + ChatColor.RED + "/roll <1-10000>");
                return true;
            }
            for (Entity ent : p.getNearbyEntities(20.0, 20.0, 20.0)) {
                Player player = (Player) sender;
                if (!(ent instanceof Player) || Gamble.disabledRollUUIDs.contains(player.getUniqueId().toString()))
                    continue;
                player.sendMessage(ChatColor.GRAY + "** " + p.getName() + ChatColor.GRAY + " has rolled a " + ChatColor.UNDERLINE + ChatColor.BOLD + roll + ChatColor.GRAY + " out of " + ChatColor.UNDERLINE + ChatColor.BOLD + args[0] + ".");
            }
            p.sendMessage(ChatColor.GRAY + "** " + p.getName() + ChatColor.GRAY + " has rolled a " + ChatColor.UNDERLINE + ChatColor.BOLD + roll + ChatColor.GRAY + " out of " + ChatColor.UNDERLINE + ChatColor.BOLD + args[0] + ".");
            return true;
        }
        return true;
    }
}

