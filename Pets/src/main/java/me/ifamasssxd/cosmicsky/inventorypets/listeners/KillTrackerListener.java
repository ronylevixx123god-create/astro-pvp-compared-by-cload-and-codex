package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.managers.InventoryPetManager;
import me.ifamasssxd.cosmicsky.inventorypets.struct.buffs.TeslaPetBuff;
import me.ifamasssxd.killtracker.events.PlayerSubtractSoulsEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KillTrackerListener implements Listener {
   private InventoryPetManager manager;

   public KillTrackerListener(InventoryPetManager manager) {
      this.manager = manager;
   }

   @EventHandler
   public void onPlayerSoulRemove(PlayerSubtractSoulsEvent event) {
      TeslaPetBuff petBuff = (TeslaPetBuff)this.manager.getTeslaBuffs().get(event.getPlayer().getUniqueId());
      if (petBuff != null && !petBuff.isExpired()) {
         event.setCancelled(true);
         if (event.getSoulsToSubtract() > 10) {
            event.getPlayer().sendMessage(CC.GreenB + "PET (" + CC.Green + "Tesla" + CC.GreenB + "): " + CC.Green + "+" + event.getSoulsToSubtract() + " souls!");
         }

         CosmicSkyInventoryPets.get().getLogger().info("Cancelling soul subtraction: " + event.getSoulsToSubtract() + " for " + event.getPlayer().getName());
      }

   }
}
