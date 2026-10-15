package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.guis.ArmorColorGUI;
import net.astropvp.core.guis.ChatColorGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatColorCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (!(player.hasPermission("astrocore.nebula"))) {
            player.sendMessage("");
            player.sendMessage(CC.translate("&c&l(!)&c You cannot use the \"/chatcolor\" command."));
            player.sendMessage(CC.translate("&7Purchase the nebula rank on &f&nbuy.astropvp.net&7!"));
            player.sendMessage("");
            return false;
        }

        ChatColorGUI chatColorGUI = new ChatColorGUI(player);
        chatColorGUI.open();
        player.sendMessage(CC.translate("&aOpening the chat color gui..."));


        return false;
    }



}