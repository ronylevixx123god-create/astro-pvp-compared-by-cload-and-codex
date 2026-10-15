package me.ifamasssxd.cosmicsky.inventorypets.listeners;

import java.util.List;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ArcaneCreeperListener implements Listener {


   public static List getBlocks(Location location, int halfWidth) {
      return getBlocks(location.getBlock(), halfWidth);
   }

   public static List getBlocks(Block block, int halfWidth) {
      int xmin = block.getX() - halfWidth;
      int ymin = block.getY() - halfWidth;
      int zmin = block.getZ() - halfWidth;
      int xmax = block.getX() + halfWidth;
      int ymax = block.getY() + halfWidth;
      int zmax = block.getZ() + halfWidth;
      return getBlocks(block.getWorld(), xmin, ymin, zmin, xmax, ymax, zmax);
   }

   public static List getBlocks(World world, int xmin, int ymin, int zmin, int xmax, int ymax, int zmax) {
      List blocks = Lists.newArrayList();

      for(int x = xmin; x <= xmax; ++x) {
         for(int y = ymin; y <= ymax; ++y) {
            for(int z = zmin; z <= zmax; ++z) {
               blocks.add(world.getBlockAt(x, y, z));
            }
         }
      }

      return blocks;
   }
}
