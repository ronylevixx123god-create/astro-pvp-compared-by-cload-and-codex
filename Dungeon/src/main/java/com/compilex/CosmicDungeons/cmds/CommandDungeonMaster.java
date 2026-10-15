/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.compilex.CosmicDungeons.cmds;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.menus.DungeonMasterKeyMenu;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDungeonMaster
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player pSender = (Player)sender;
            new DungeonMasterKeyMenu(pSender).openGUI(CosmicDungeonsPlugin.get());
            pSender.playSound(pSender.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.8f);
        }
        return true;
    }
}

