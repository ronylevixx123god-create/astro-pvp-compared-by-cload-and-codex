package me.ifamasssxd.cosmicsky.inventorypets.managers;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.buffs.TeslaPetBuff;
import me.ifamasssxd.cosmicsky.inventorypets.struct.buffs.XpPetBuff;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventoryPetManager {
   private CosmicSkyInventoryPets plugin;

   private Map<UUID, XpPetBuff> petBuffExpiration = new ConcurrentHashMap<>();

   public Map<UUID, XpPetBuff> getPetBuffExpiration() {
      return this.petBuffExpiration;
   }

   private Map<UUID, TeslaPetBuff> teslaBuffs = new ConcurrentHashMap<>();

   public Map<UUID, TeslaPetBuff> getTeslaBuffs() {
      return this.teslaBuffs;
   }

   public InventoryPetManager(CosmicSkyInventoryPets plugin) {
      this.plugin = plugin;
   }

   public void onEnable(CosmicSkyInventoryPets plugin) {
      plugin.getServer().getScheduler().runTaskTimer((Plugin)plugin, () -> {
         for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            boolean updated = false;
            for (int i = 0; i < 9; i++) {
               ItemStack hotbar = player.getInventory().getItem(i);
               if (hotbar != null && hotbar.getType() != Material.AIR) {
                  NBTWrapper wrapper = new NBTWrapper(hotbar);
                  InventoryPetItem item = (InventoryPetItem)CosmicPersistentItem.constructItem(hotbar, wrapper, InventoryPetItem.class);
                  if (item != null && item.getPetType() != null && item.getPetType().getInternalClass().onTickInHotBar(player, i, item)) {
                     player.getInventory().setItem(i, item.build());
                     updated = true;
                  }
               }
            }
            if (updated)
               player.updateInventory();
         }
      },20L, 20L);

   }

   public XpPetBuff getBuff(Player player) {
      return this.petBuffExpiration.get(player.getUniqueId());
   }

   public void removeBuff(Player player) {
      this.petBuffExpiration.remove(player.getUniqueId());
   }
}
