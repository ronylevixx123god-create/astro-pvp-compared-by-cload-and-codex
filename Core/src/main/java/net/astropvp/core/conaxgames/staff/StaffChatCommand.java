package net.astropvp.core.conaxgames.staff;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StaffChatCommand extends Command {

    public StaffChatCommand() {
        super("staffchat");
        setAliases(Arrays.asList("sc", "staffc", "chatstaff"));
        setUsage(ChatColor.RED + "/sc");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Player command.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("astrocore.staff")) {
            player.sendMessage(CC.Red + "No Permission.");
            return false;
        }
        StringBuilder text = new StringBuilder();
        for (String arg : strings) {
            text.append(arg).append(" ");
        }
        if (text.toString().isEmpty()) {
            if (StaffUtil.inStaffChat.contains(player)) {
                StaffUtil.inStaffChat.remove(player);
                player.sendMessage(CC.Red + "You have left staff chat.");
            } else {
                StaffUtil.inStaffChat.add(player);
                player.sendMessage(CC.Green + "You are now in staff chat.");
            }
            return false;
        }
        StaffChat.sendStaffMessage(player, text.toString());
        return false;
    }
}