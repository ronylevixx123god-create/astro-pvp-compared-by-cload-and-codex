package org.arkhamnetwork.enchantments.cmds;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(CC.Red + "No Perms.");
            return false;
        }

        Enchantments.getInstance().getConfigManager().reloadFiles();
        sender.sendMessage(CC.translate("&aReloaded config..."));
        return false;

    }

}
