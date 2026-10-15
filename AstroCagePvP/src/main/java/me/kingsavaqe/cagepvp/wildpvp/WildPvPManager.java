package me.kingsavaqe.cagepvp.wildpvp;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WildPvPManager {
   private final AstroCagePvP instance;
   private List<WildPvP> wildPvPs = new ArrayList();

   public WildPvPManager(AstroCagePvP instance) {
      this.instance = instance;
   }

   public void onDisable() {

      for(WildPvP wildPvP : this.wildPvPs) {
         wildPvP.hideBorder(wildPvP.getCreator());
         if(wildPvP.hasJoined()) {
            wildPvP.teleportToJoinLocation(wildPvP.getJoined());
            wildPvP.hideBorder(wildPvP.getJoined());
         }
      }

   }

   public WildPvP createWildPvP(Player player, boolean teleport) {
      WildPvP wildPvP = new WildPvP(this.instance, player, player.getLocation().clone());
      if (teleport) {
         wildPvP.teleport(player);
      }

      this.wildPvPs.add(wildPvP);
      return wildPvP;
   }

   public void removeWildPvP(WildPvP wildPvP) {
      this.wildPvPs.remove(wildPvP);
   }

   public WildPvP getWildPvP(Player player) {
      Iterator var2 = this.wildPvPs.iterator();

      WildPvP wildPvP;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         wildPvP = (WildPvP)var2.next();
      } while(!wildPvP.isCreator(player) && !wildPvP.isJoined(player));

      return wildPvP;
   }

   public boolean hasWildPvP(Player player) {
      return this.getWildPvP(player) != null;
   }

   public WildPvP getWildPvP(Chunk chunk) {
      Iterator var2 = this.wildPvPs.iterator();

      WildPvP wildPvP;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         wildPvP = (WildPvP)var2.next();
      } while(wildPvP.getChunk().getX() != chunk.getX() || wildPvP.getChunk().getZ() != chunk.getZ());

      return wildPvP;
   }

   public boolean hasWildPvP(Chunk chunk) {
      return this.getWildPvP(chunk) != null;
   }

   public List<WildPvP> getActiveWildPvPs() {
      List<WildPvP> activeWildPvPs = new ArrayList();

      for(WildPvP wildPvP : this.wildPvPs) {
         if(wildPvP.hasJoined()) {
            activeWildPvPs.add(wildPvP);
         }
      }

      return activeWildPvPs;
   }

   public List<WildPvP> getPendingWildPvPs() {
      List<WildPvP> pendingWildPvPs = new ArrayList();

      for(WildPvP wildPvP : this.wildPvPs) {
         if(!wildPvP.hasJoined()) {
            pendingWildPvPs.add(wildPvP);
         }
      }

      return pendingWildPvPs;
   }

   public List<WildPvP> getWildPvPs() {
      return this.wildPvPs;
   }
}
