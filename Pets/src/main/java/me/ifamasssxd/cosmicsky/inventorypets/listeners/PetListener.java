package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PetListener implements Listener {
   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPreCommand(PlayerCommandPreprocessEvent event) {
      if (event.getMessage().toLowerCase().startsWith("/help pet")) {
         event.setMessage("/pet help");
      }

   }
}
