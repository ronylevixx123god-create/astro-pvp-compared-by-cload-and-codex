package net.astropvp.core.conaxgames.staff;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffChat {
    public static void sendStaffMessage(Player sender, String msg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("astrocore.staff")) {
                player.sendMessage(StaffUtil.format + CC.Yellow + sender.getName() + ": " + CC.LightPurple + msg);
            }
        });
    }
    public static void sendGlobal(String msg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("astrocore.staff")) {
                player.sendMessage(StaffUtil.format + CC.Gray + msg);
            }
        });
    }
}
