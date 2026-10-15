/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.TimeUtils
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.astropvp.core.autogiveall.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.autogiveall.struct.GiveAllManager;
import org.arkhamnetwork.Arkkit.utils.TimeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandNextGiveAll
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        boolean seconds = false;
        seconds = GiveAllManager.getNextGiveAll() < 60L;
        if (seconds) {
            player.sendMessage(CC.GreenB + "(!) Next Giveall: " + GiveAllManager.getNextGiveAll() + "s");
        } else {
            player.sendMessage(CC.Green + "Next Giveall: " + TimeUtils.formatDifference((long)GiveAllManager.getNextGiveAll()));
        }
        return false;
    }
}

