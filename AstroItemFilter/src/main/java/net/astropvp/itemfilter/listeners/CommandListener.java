package net.astropvp.itemfilter.listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onCommandUse( PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/filter ") || e.getMessage().equalsIgnoreCase("/filter")) {
            e.setCancelled(true);
            Bukkit.dispatchCommand((CommandSender)e.getPlayer(), e.getMessage().replace("/filter", "filters"));
        }
    }
}
