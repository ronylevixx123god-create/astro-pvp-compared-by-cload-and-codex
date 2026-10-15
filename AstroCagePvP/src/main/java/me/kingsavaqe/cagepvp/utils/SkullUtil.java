package me.kingsavaqe.cagepvp.utils;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtil {
   public static ItemStack getPlayerSkull(Player player) {
      ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
      SkullMeta sm = (SkullMeta)is.getItemMeta();

      try {
         Object EntityPlayer = player.getClass().getMethod("getHandle").invoke(player);
         NMSUtil.setField(sm, "profile", NMSUtil.getFieldObject(EntityPlayer, NMSUtil.getField(EntityPlayer.getClass().getSuperclass(), "bH", true)), true);
      } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException var4) {
         var4.printStackTrace();
      }

      is.setItemMeta(sm);
      return is;
   }
}
