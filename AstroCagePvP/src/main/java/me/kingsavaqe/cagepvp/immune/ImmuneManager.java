package me.kingsavaqe.cagepvp.immune;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import org.bukkit.entity.Player;

public class ImmuneManager {
   private final AstroCagePvP instance;
   private List<Immune> immunes = new ArrayList();

   public ImmuneManager(AstroCagePvP instance) {
      this.instance = instance;
   }

   public Immune createImmune(Player player) {
      Immune immune = new Immune(this.instance, player);
      this.immunes.add(immune);
      return immune;
   }

   public void removeImmune(Immune immune) {
      this.immunes.remove(immune);
   }

   public void removeImmune(Player player) {
      Iterator immuneIterator = this.immunes.iterator();

      while(immuneIterator.hasNext()) {
         Immune immune = (Immune)immuneIterator.next();
         if (immune.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            immuneIterator.remove();
            break;
         }
      }

   }

   public Immune getImmune(Player player) {
      Iterator var2 = this.immunes.iterator();

      Immune immune;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         immune = (Immune)var2.next();
      } while(!immune.getPlayer().getUniqueId().equals(player.getUniqueId()));

      return immune;
   }

   public boolean hasImmune(Player player) {
      return this.getImmune(player) != null;
   }
}
