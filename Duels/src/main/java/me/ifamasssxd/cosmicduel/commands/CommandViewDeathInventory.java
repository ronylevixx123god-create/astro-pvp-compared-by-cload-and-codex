/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 */
package me.ifamasssxd.cosmicduel.commands;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.DuelHistoryManager;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class CommandViewDeathInventory
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return true;
        }
        String uuidStr = args[0];
        Player player = (Player)sender;
        if (PlayerUtils.inCombat(player)) {
            player.sendMessage((Object)ChatColor.RED + "You cannot do this while in combat.");
            return true;
        }
        if (DuelAPI.hasDuel(player)) {
            player.sendMessage((Object)ChatColor.RED + "You cannot do this while in a duel.");
            return true;
        }
        try {
            UUID uuid = UUID.fromString(uuidStr);
            DuelHistoryManager manager = CosmicDuel.get().getDuelHistoryManager();
            if (manager.getPreviousInventories().containsKey(uuid)) {
                Inventory inventory = manager.getStoredInventory(uuid);
                player.openInventory(inventory);
            }
        }
        catch (Exception e) {
            return true;
        }
        return false;
    }
}

