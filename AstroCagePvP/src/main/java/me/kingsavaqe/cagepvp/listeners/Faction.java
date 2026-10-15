package me.kingsavaqe.cagepvp.listeners;

import com.massivecraft.factions.event.LandClaimEvent;
import me.kingsavaqe.cagepvp.AstroCagePvP;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Faction implements Listener {
   private final AstroCagePvP instance;

   public Faction(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onLandClaim(LandClaimEvent event) {
      if (event.getLocation() != null) {
         if (this.instance.getWildPvPManager().hasWildPvP(event.getLocation().getChunk())) {
            if (event.getfPlayer() != null && event.getfPlayer().getPlayer() != null) {
               event.setCancelled(true);
               Player player = event.getfPlayer().getPlayer();
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("Listeners.Claim.Match.Message")));
               player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
            } else {
               event.setCancelled(true);
            }
         }
      }
   }
}
