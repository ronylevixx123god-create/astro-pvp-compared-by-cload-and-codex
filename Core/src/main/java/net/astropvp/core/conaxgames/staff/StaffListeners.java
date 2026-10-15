package net.astropvp.core.conaxgames.staff;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class StaffListeners implements Listener {
    // Staff chat
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        try {
            if (StaffUtil.inStaffChat.contains(event.getPlayer())) {
                StaffChat.sendStaffMessage(event.getPlayer(), event.getMessage());
                event.setCancelled(true);
            }
        } catch (Exception e) {

        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (StaffUtil.inStaffChat.contains(event.getPlayer())) {
            event.getPlayer().sendMessage(StaffUtil.format + CC.Gray + "You have been put in staff chat as you logged out with it enabled.");
        }
    }
}
