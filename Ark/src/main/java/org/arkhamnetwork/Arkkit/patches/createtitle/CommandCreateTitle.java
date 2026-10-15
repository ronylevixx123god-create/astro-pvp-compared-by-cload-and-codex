/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.libs.TitleItems
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package org.arkhamnetwork.Arkkit.patches.createtitle;

import com.cosmicpvp.CosmicSpaceChests.libs.TitleItems;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class CommandCreateTitle
        extends AbstractCommand {
    public CommandCreateTitle(String command, String usage, String description) {
        super(command, usage, description);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player pl;
        if (!sender.isOp()) {
            return true;
        }
        Player player = pl = sender instanceof Player ? (Player) sender : null;
        if (args.length != 1 || pl == null) {
            sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Usage " + ChatColor.WHITE + "/createtitle <title>");
            sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Title creation cannot include spaces. Use _ as a placeholder for spaces.");
            return true;
        }
        String title = args[0];
        pl.getInventory().addItem(TitleItems.getTitleItem(title));
        return true;
    }
}

