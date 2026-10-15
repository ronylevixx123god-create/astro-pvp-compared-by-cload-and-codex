package me.kingsavaqe.cagepvp.listeners;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Move implements Listener {
   private final AstroCagePvP instance;

   public Move(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler
   public void onPlayerMoveOutsideChunk(PlayerMoveEvent event) {
      Player player = event.getPlayer();
      Location fromLocation = event.getFrom();
      Location toLocation = event.getTo();
      WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
      if (wildPvPManager.hasWildPvP(player)) {
         if (player.isOp()) return;
         WildPvP wildPvP = wildPvPManager.getWildPvP(player);
         if (fromLocation.getChunk().getX() == wildPvP.getChunk().getX() && fromLocation.getChunk().getZ() == wildPvP.getChunk().getZ() && (toLocation.getChunk().getX() != wildPvP.getChunk().getX() || toLocation.getChunk().getZ() != wildPvP.getChunk().getZ())) {
            Location location = player.getLocation().clone().add(fromLocation.toVector().subtract(toLocation.toVector()).normalize().multiply(2.0D));
            if (location.getBlock().getType().isSolid()) {
               location.setY(location.getWorld().getHighestBlockYAt(location));
            }

            player.teleport(location, TeleportCause.UNKNOWN);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("Listeners.Move.Chunk.Participant.Message")));
            player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1.0F, 5.0F);
         }
      }
   }

   @EventHandler
   public void onPlayerMoveInsideChunk(PlayerMoveEvent event) {
      Player player = event.getPlayer();
      if (player.getGameMode() != GameMode.SPECTATOR) {
         Location fromLocation = event.getFrom();
         Location toLocation = event.getTo();
         WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
         if (wildPvPManager.hasWildPvP(toLocation.getChunk())) {
            if (player.isOp()) return;
            if (player.hasPermission("essentials.tp.others")) return;

            WildPvP wildPvP = wildPvPManager.getWildPvP(toLocation.getChunk());
            if (!wildPvP.isParticipant(player)) {
               Location location = player.getLocation().clone().add(fromLocation.toVector().subtract(toLocation.toVector()).normalize().multiply(2.0D));
               if (location.getBlock().getType().isSolid()) {
                  location.setY(location.getWorld().getHighestBlockYAt(location));
               }

               player.teleport(location, TeleportCause.UNKNOWN);
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.instance.getConfig().getString("Listeners.Move.Chunk.Match.Message")));
               player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1.0F, 5.0F);
            }
         }
      }
   }
}
