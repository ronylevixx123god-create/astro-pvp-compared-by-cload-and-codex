/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.compilex.CosmicKOTH.cmds;

import com.compilex.CosmicKOTH.CosmicKOTHAPI;
import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.compilex.CosmicKOTH.utils.LocationUtils;
import com.compilex.CosmicKOTH.utils.RewardUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandAdminKoth
implements CommandExecutor {
    @SneakyThrows
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        String param1 = args[0];
        if (param1.equalsIgnoreCase("start")) {
            CosmicKOTHAPI.resetKOTH();
            sender.sendMessage("(A-KOTH) KOTH has been reset.");
        }
        if (param1.equalsIgnoreCase("forcecap")) {
            if (CosmicKOTHPlugin.get_().getKoth() == null) {
                sender.sendMessage(CC.Red + "No KOTH currently running!");
                return true;
            }
            Player player = (Player)(args.length > 1 ? Bukkit.getPlayer((String)args[1]) : sender);
            if (player == null) {
                sender.sendMessage(CC.Red + "Invalid player entered!");
                return true;
            }
            player.sendMessage(CC.Red + "Force capturing koth..");
            CosmicKOTHPlugin.get_().getKoth().captureKOTH(player);
            return true;
        }
        if (param1.equalsIgnoreCase("loot")) {
            if (sender instanceof Player) {
                ((Player)sender).getInventory().addItem(new ItemStack[]{RewardUtils.getKOTHLootbag((Player)sender)});
                sender.sendMessage("(A-KOTH) Lootbag added.");
            }
        } else if (param1.equalsIgnoreCase("logwin")) {
            CosmicKOTHPlugin.get_().getKoth().logWinner((Player)sender);
            sender.sendMessage((Object)ChatColor.RED + "Forced log win in .log!");
        }
        if (param1.equalsIgnoreCase("setspawn")) {
            Location newSpawn = ((Player)sender).getLocation();
            CosmicKOTHPlugin.get_().setSpawn(newSpawn);
            CosmicKOTHPlugin.get_().getConfig().set("playerSpawn", (Object)LocationUtils.convertLocationToString(newSpawn));
            CosmicKOTHPlugin.get_().saveConfig();
            sender.sendMessage(CC.Red + "KOTH Spawn set to your location!");
            return true;
        }
        if (param1.equalsIgnoreCase("setkothcenter")) {
            Location newSpawn = ((Player)sender).getLocation();
            CosmicKOTHPlugin.get_().setKothCenter(newSpawn);
            CosmicKOTHPlugin.get_().getConfig().set("kothCenter", (Object)LocationUtils.convertLocationToString(newSpawn));
            CosmicKOTHPlugin.get_().saveConfig();
            sender.sendMessage(CC.Red + "KOTH Center set to your location!");
        }
        return true;
    }
}

