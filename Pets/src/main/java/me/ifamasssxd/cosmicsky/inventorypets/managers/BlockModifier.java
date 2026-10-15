package me.ifamasssxd.cosmicsky.inventorypets.managers;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockModifier {
   private static HashMap toRemove = new HashMap();
   private HashMap toReplace = new HashMap();
   Random random = new Random();

   public boolean containsLocation(Location l) {
      Iterator var2 = toRemove.values().iterator();

      List listLoc;
      do {
         if (!var2.hasNext()) {
            var2 = this.toReplace.values().iterator();

            HashMap loc;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               loc = (HashMap)var2.next();
            } while(!loc.containsKey(l));

            return true;
         }

         listLoc = (List)var2.next();
      } while(!listLoc.contains(l));

      return true;
   }

   public boolean containsLocation(UUID uuid, Location l) {
      if (toRemove.containsKey(uuid)) {
         return ((List)toRemove.get(uuid)).contains(l);
      } else {
         return this.toReplace.containsKey(uuid) && ((HashMap)this.toReplace.get(uuid)).containsKey(l);
      }
   }

   public void removeBlock(UUID uuid, Location loc) {
      List removed = (List)toRemove.get(uuid);
      Block b = loc.getBlock();
      b.setType(Material.AIR);
      removed.remove(loc);
      toRemove.put(uuid, removed);
   }

   public static void removeAllBlocks(boolean particles) {
      Iterator var1 = toRemove.keySet().iterator();

      while(var1.hasNext()) {
         UUID key = (UUID)var1.next();
         removeBlocks(key, false, particles);
      }

      toRemove.clear();
   }

   public static void removeBlocks(UUID uuid, boolean remove, boolean particles) {
      if (toRemove.containsKey(uuid)) {
         Block b;
         for(Iterator var3 = ((List)toRemove.get(uuid)).iterator(); var3.hasNext(); b.setType(Material.AIR)) {
            Location l = (Location)var3.next();
            b = l.getBlock();
            if (particles && b.getType().isSolid()) {
               b.getType().isBlock();
            }
         }

         if (remove) {
            toRemove.remove(uuid);
         }

      }
   }

   public void replaceAll() {
      Iterator var1 = this.toReplace.keySet().iterator();

      while(var1.hasNext()) {
         UUID key = (UUID)var1.next();
         this.replaceBlocks(key, false);
      }

      this.toReplace.clear();
   }

   public void replaceBlocks(UUID uuid, boolean removeAfter) {
      if (this.toReplace.containsKey(uuid)) {
         HashMap data = (HashMap)this.toReplace.get(uuid);
         Iterator var4 = data.entrySet().iterator();

         while(var4.hasNext()) {
            Entry blockData = (Entry)var4.next();
            Block b = ((Location)blockData.getKey()).getBlock();
            NewBlockData newBlockDAta = (NewBlockData)blockData.getValue();
            b.setType(newBlockDAta.getMaterial());
            b.setData(newBlockDAta.getData());
         }

         if (removeAfter) {
            this.toReplace.remove(uuid);
         }

      }
   }

   public void restoreBlock(UUID uuid, Block block) {
      Map data = (Map)this.toReplace.get(uuid);
      if (data != null) {
         NewBlockData blockData = (NewBlockData)data.get(block.getLocation());
         if (blockData != null) {
            block.setTypeIdAndData(blockData.getMaterial().getId(), blockData.getData(), false);
         }
      }
   }

   public NewBlockData removeBlockAndStore(UUID uuid, Location location) {
      Map storedBlocks = (Map)this.toReplace.computeIfAbsent(uuid, (map) -> {
         return Maps.newHashMap();
      });
      Block block = location.getBlock();
      NewBlockData stored = new NewBlockData(block.getType(), block.getData());
      storedBlocks.put(block.getLocation(), stored);
      block.setTypeIdAndData(Material.AIR.getId(), (byte)0, true);
      return stored;
   }

   public NewBlockData storeBlockAndSet(UUID uuid, Location location, Material material, byte data) {
      Map storedBlocks = (Map)this.toReplace.computeIfAbsent(uuid, (map) -> {
         return Maps.newHashMap();
      });
      Block block = location.getBlock();
      NewBlockData stored = new NewBlockData(block.getType(), block.getData());
      storedBlocks.put(block.getLocation(), stored);
      block.setTypeIdAndData(material.getId(), data, false);
      return stored;
   }

   public void storeBlockToRemove(UUID uuid, Location l) {
      List locs = toRemove.containsKey(uuid) ? (List)toRemove.get(uuid) : Lists.newArrayList();
      ((List)locs).add(l.clone());
      toRemove.put(uuid, locs);
   }
}
