/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.itembuilder.FatBucketBuilder
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.itembuilder.FatBucketBuilder;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class CommandFatBucket
        extends AbstractCommand {
    public CommandFatBucket(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("/fatbucket <player>");
            return true;
        }
        Player p = Bukkit.getPlayer(args[0]);
        if (p != null) {
            p.getInventory().addItem(new FatBucketBuilder().build());
            sender.sendMessage("Fat bucket added to " + p.getName() + "'s inventory");
        }
        return false;
    }
}

