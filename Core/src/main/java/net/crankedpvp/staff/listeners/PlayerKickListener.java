package net.crankedpvp.staff.listeners;

import net.astropvp.core.AstroCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKickListener implements Listener {

    private final AstroCore plugin;
    public PlayerKickListener(AstroCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        if(event.getPlayer() == null) return;
        if(!this.plugin.getStaffManager().isPlayerInStaffMode(event.getPlayer())) return;
        this.plugin.getStaffManager().getStaffModeFromPlayer(event.getPlayer()).removePlayerFromStaffMode();
    }
}
