package me.kingsavaqe.cagepvp.listeners;

import java.util.Iterator;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class Item implements Listener {
   private final AstroCagePvP instance;

   public Item(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onPlayerPickupItem(PlayerPickupItemEvent event) {
      if (!event.isCancelled()) {
         Player player = event.getPlayer();
         if (this.instance.getWildPvPManager().hasWildPvP(player)) {

            //for(String item : this.instance.getConfig().getStringList("WildPvP.Match.ItemPickupBlacklist")) {
               //if(event.getItem().getItemStack().getType().name().equalsIgnoreCase(item)) {
                  event.setCancelled(true);
                  //break;
               //}
            //}

         }
      }
   }
}
