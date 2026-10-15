package net.astropvp.core.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.astropvp.core.utils.ChatManager;
import net.astropvp.core.utils.InvseeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Invsee extends BaseCommand {

    @CommandAlias("invsee|inventorysee")
    @Syntax("<player> &e- &7Invsee players.")
    public void onCommand(Player player, OnlinePlayer target) {
        if (target.player == null) {
            player.sendMessage(ChatManager.translate("&cInvalid player."));
            return;
        }

        InvseeManager inventoryManager = new InvseeManager(player, target.player);
        player.openInventory(inventoryManager.getInventory());
    }
}
