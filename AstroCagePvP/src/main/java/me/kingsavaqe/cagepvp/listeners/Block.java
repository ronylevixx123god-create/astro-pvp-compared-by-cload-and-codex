package me.kingsavaqe.cagepvp.listeners;

import java.util.Iterator;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class Block implements Listener {
   private final AstroCagePvP instance;

   public Block(AstroCagePvP instance) {
      this.instance = instance;
   }

   @EventHandler(priority = EventPriority.LOWEST)
   public void onBlockPlace(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
      FileConfiguration configLoad = this.instance.getConfig();
      if (wildPvPManager.hasWildPvP(player)) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Block.Place.Match.Message")));
         player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
      } else if (wildPvPManager.hasWildPvP(event.getBlock().getChunk())) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Block.Place.Chunk.Message")));
         player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
      }

   }

   @EventHandler(priority = EventPriority.LOWEST)
   public void onBlockBreak(BlockBreakEvent event) {
      Player player = event.getPlayer();
      WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
      FileConfiguration configLoad = this.instance.getConfig();
      if (wildPvPManager.hasWildPvP(player)) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Block.Break.Match.Message")));
         player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
      } else if (wildPvPManager.hasWildPvP(event.getBlock().getChunk())) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Block.Break.Chunk.Message")));
         player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
      }

   }

   @EventHandler(priority = EventPriority.LOWEST)
   public void onEntityExplode(EntityExplodeEvent event) {
      WildPvPManager wildPvPManager = this.instance.getWildPvPManager();
      if (!this.instance.getConfig().getBoolean("WildPvP.EntityExplosion.Enable")) {
         Iterator blockListIterator = event.blockList().iterator();

         while(blockListIterator.hasNext()) {
            org.bukkit.block.Block block = (org.bukkit.block.Block)blockListIterator.next();
            if (wildPvPManager.hasWildPvP(block.getChunk())) {
               blockListIterator.remove();
            }
         }

      }
   }
}
