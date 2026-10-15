package me.ifamasssxd.cosmicsky.inventorypets.utils;

import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import java.util.Arrays;
import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
   public static ItemStack buildItem(Material m, short metaData, String name) {
      ItemStack is = new ItemStack(m, 1, metaData);
      ItemMeta im = is.getItemMeta();
      if (name != null) {
         im.setDisplayName(name);
      }

      is.setItemMeta(im);
      return is;
   }

   public static ItemStack buildItem(Material m, short metaData, String name, String... description) {
      ItemStack is = new ItemStack(m, 1, metaData);
      ItemMeta im = is.getItemMeta();
      if (name != null) {
         im.setDisplayName(name);
      }

      if (description != null) {
         im.setLore(Arrays.asList(description));
      }

      is.setItemMeta(im);
      return is;
   }

   public static ItemStack buildItem(Material m, short metaData, String name, LinkedList description) {
      ItemStack is = new ItemStack(m, 1, metaData);
      ItemMeta im = is.getItemMeta();
      if (name != null) {
         im.setDisplayName(name);
      }

      if (description != null) {
         im.setLore(description);
      }

      is.setItemMeta(im);
      return is;
   }

   public static ItemStack setAmount(ItemStack i, int x) {
      i.setAmount(x);
      return i;
   }

   public static ItemStack addGlow(ItemStack item) {
      return (new NBTWrapper(item)).setGlowing().build();
   }

   public static void giveItemToPlayer(Player p, ItemStack i) {
      if (p.getInventory().firstEmpty() != -1) {
         p.getInventory().addItem(new ItemStack[]{i});
      } else {
         Bukkit.getLogger().info("[CosmicDungeons] " + p.getName() + " did not have me.astropvp.cratekeys.inventory space for " + i + ", dropping on ground!");
         p.getWorld().dropItem(p.getLocation(), i);
      }

   }
}
