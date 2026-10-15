package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.managers.InventoryPetManager;
import me.ifamasssxd.cosmicsky.inventorypets.struct.buffs.XpPetBuff;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class PetLogicListener implements Listener {
   private InventoryPetManager manager;

   public PetLogicListener(InventoryPetManager manager) {
      this.manager = manager;
   }

   @EventHandler
   public void blockBreak(final BlockBreakEvent e) {
      final Block block = e.getBlock();
      if (block.getType() == Material.OBSIDIAN || block.getType() == Material.IRON_FENCE) {
         if (block.hasMetadata("cage")) {
            e.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void tnt(final EntityExplodeEvent e) {
      final List<Block> list = (List<Block>)e.blockList();
      for (int i = 0; i < list.size(); ++i) {
         final Block block = list.get(i);
         if (block.getType() == Material.OBSIDIAN || block.getType() == Material.IRON_FENCE) {
            if (block.hasMetadata("cage")) {
               list.remove(i);
            }
         }
      }
   }

   @EventHandler(priority = EventPriority.HIGHEST)
   public void onKnockback(final EntityDamageEvent event) {
      if (event.isCancelled()) {
         return;
      }
      if (event.getEntity() instanceof Player) {
         final Player player = (Player)event.getEntity();
         if (event.getEntity().hasMetadata("no_knockback")) {
            final Vector vec = new Vector();
            player.setVelocity(vec);
            new BukkitRunnable() {
               public void run() {
                  player.setVelocity(vec);
               }
            }.runTaskLater((Plugin)CosmicSkyInventoryPets.get(), 1L);
         }
      }
   }


   @EventHandler
   public void blockPlace(BlockPlaceEvent e) {
      if (e.getBlock().getType() == Material.SKULL_ITEM || e.getBlock().getType() == Material.SKULL) {
         e.setCancelled(true);
      }
   }
}
