/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NumberUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package net.astropvp.core.killtracker.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGiveSoulGem
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 2) {
            Player player = Bukkit.getPlayer((String)args[0]);
            if (player == null) {
                sender.sendMessage(CC.Red + "Invalid player given!");
                return true;
            }
            Integer given = NumberUtils.parseIntOrNull((String)args[1]);
            if (given == null || given <= 0 || (double)given.intValue() >= 1.0E10) {
                sender.sendMessage(CC.Red + "Please enter a valid amount of souls!");
                return true;
            }
            PlayerUtil.giveOrDropProtectedItem((Player)player, (ItemStack) KillTrackerAPI.createSoulShard(given), (int)-1);
            sender.sendMessage(CC.Red + "Soul Tracker [" + given + "] given to " + player.getName() + "!");
            return true;
        }
        sender.sendMessage(CC.Red + "Usage: /givesoulgem <player> <amount>");
        return false;
    }
}

