package net.crankedpvp.staff.managers;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StaffChatManager {
    private final List<Player> inStaffChat;
    private final String FORMAT = CC.DarkPurple + "[Staff] ";

    public StaffChatManager() {
        this.inStaffChat = new ArrayList<>();
    }

    public  void sendStaffMessage(Player sender, String msg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("astrocore.staffchat")) {
                player.sendMessage(FORMAT + CC.Yellow + sender.getName() + ": " + CC.LightPurple + msg);
            }
        });
    }
    public  void sendStaffMessage(String msg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.hasPermission("astrocore.staffchat")) {
                player.sendMessage(this.FORMAT + CC.Gray + msg);
            }
        });
    }
}
