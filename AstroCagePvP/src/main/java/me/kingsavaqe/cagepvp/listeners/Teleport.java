package me.kingsavaqe.cagepvp.listeners;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Teleport implements Listener {
   private final AstroCagePvP instance;

   public Teleport(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onPlayerTeleportParticipant(PlayerTeleportEvent event) {
      if (!event.isCancelled()) {
         if (event.getCause() != TeleportCause.UNKNOWN) {
            Player player = event.getPlayer();
            if (this.instance.getWildPvPManager().hasWildPvP(player)) {
               if (player.isOp()) return;
               if (player.hasPermission("essentials.tp.others")) return;
               event.setCancelled(true);
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("Listeners.Teleport.Match.Participant.Message")));
               player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
            }
         }
      }
   }

   @EventHandler
   public void onPlayerTeleportMatch(PlayerTeleportEvent event) {
      if (!event.isCancelled()) {
         if (event.getCause() != TeleportCause.UNKNOWN) {
            Player player = event.getPlayer();
            if (player.getGameMode() != GameMode.SPECTATOR) {

               WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
               if (wildPvPManager.hasWildPvP(event.getTo().getChunk())) {
                  if (player.isOp()) return;
                  if (player.hasPermission("essentials.tp.others")) return;

                  if (!wildPvPManager.getWildPvP(event.getTo().getChunk()).isParticipant(player)) {
                     event.setCancelled(true);
                     player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("Listeners.Teleport.Match.Match.Message")));
                     player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                  }
               }
            }
         }
      }
   }
}
