package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Broadcast extends Command {

    public Broadcast() {
        super("astrobc");
        setAliases(Arrays.asList("astrobroadcast", "abcast", "abc"));
        setUsage(ChatColor.RED + "/bc");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("Player command.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.isOp()) {
            player.sendMessage(CC.Red + "No Permission.");
            return false;
        }
        if (!AstroCore.getInstance().getConfig().getBoolean("broadcast.enabled")) {
            player.sendMessage(CC.Red + "Broadcast command has been disabled within the configuration.");
            return false;
        }
        StringBuilder text = new StringBuilder();
        for (String arg : strings) {
            text.append(arg).append(" ");
        }
        if (text.toString().isEmpty()) {
            player.sendMessage(CC.Red + "Message was empty, not sending it.");
            return false;
        }
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(player.getLocation(), Sound.HORSE_SADDLE, 1.0F, 1.0F);
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c "));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lAstro&f&lPvP &8| &6" + text.toString()));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c "));
        return false;
    }
}