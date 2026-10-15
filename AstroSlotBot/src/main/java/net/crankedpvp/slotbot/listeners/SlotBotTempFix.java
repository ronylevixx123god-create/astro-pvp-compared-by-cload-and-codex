package net.crankedpvp.slotbot.listeners;

import net.crankedpvp.slotbot.SlotBotPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class SlotBotTempFix implements Listener {

    private SlotBotPlugin plugin;
    public SlotBotTempFix(SlotBotPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerJoin(PlayerJoinEvent event) {
        if(event.getPlayer() == null) return;
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
            if(!event.getPlayer().isOnline()) return;
            event.getPlayer().removeMetadata("SlotBotReturnTickets", this.plugin);
        }, 50L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void playerQuit(PlayerQuitEvent event) {
        if(event.getPlayer() == null) return;
        event.getPlayer().setMetadata("SlotBotReturnTickets", new FixedMetadataValue(this.plugin, "SlotBotReturnTickets"));
    }
}
