package me.kingsavaqe.cagepvp.listeners;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class Vehicle implements Listener {
   private final AstroCagePvP instance;

   public Vehicle(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onVehicleEnter(VehicleEnterEvent event) {
      if (event.getEntered() instanceof Player) {
         Player player = (Player)event.getEntered();
         if (this.instance.getWildPvPManager().hasWildPvP(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("Listeners.Vehicle.Match.Message")));
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
         }
      }
   }
}
