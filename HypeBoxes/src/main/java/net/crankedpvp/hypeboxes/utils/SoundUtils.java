package net.crankedpvp.hypeboxes.utils;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void playSoundIfPlayer(CommandSender commandSender, Sound sound) {
        if(commandSender instanceof Player) {
            Player player = ((Player)commandSender);
            player.playSound(player.getLocation(), sound, 2.0F, 2.0F);
        }
    }

    public static void tryPlaySuccessSound(CommandSender commandSender) {
        playSoundIfPlayer(commandSender, Sound.SUCCESSFUL_HIT);
    }

    public static void tryPlayErrorSound(CommandSender commandSender) {
        playSoundIfPlayer(commandSender, Sound.ANVIL_LAND);
    }

    public static void playSuccessSound(Player player) {
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
    }

    public static void playErrorSound(Player player) {
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);
    }

}
