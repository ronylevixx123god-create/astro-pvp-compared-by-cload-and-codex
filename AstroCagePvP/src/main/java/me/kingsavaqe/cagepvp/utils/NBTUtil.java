package me.kingsavaqe.cagepvp.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.inventory.ItemStack;

public class NBTUtil {
   private static final Class<?> CraftItemStackClass = NMSUtil.getCraftClass("inventory.CraftItemStack");
   private static final Class<?> NBTTagCompoundClass = NMSUtil.getNMSClass("NBTTagCompound");
   private static final Class<?> NBTTagStringClass = NMSUtil.getNMSClass("NBTTagString");
   private static final Class<?> NBTBaseClass = NMSUtil.getNMSClass("NBTBase");

   public static ItemStack addTag(ItemStack item, String tag, String data) {
      try {
         Object NMSItemStack = CraftItemStackClass.getMethod("asNMSCopy", ItemStack.class).invoke((Object)null, item);
         Field tagField = NMSItemStack.getClass().getDeclaredField("tag");
         tagField.setAccessible(true);
         Object NBTTagCompound = tagField.get(NMSItemStack);
         if (NBTTagCompound == null) {
            NBTTagCompound = NBTTagCompoundClass.newInstance();
         }

         Object NBTTagString = NBTTagStringClass.getConstructor(String.class).newInstance(data);
         Method setMethod = NBTTagCompoundClass.getMethod("set", String.class, NBTBaseClass);
         setMethod.setAccessible(true);
         setMethod.invoke(NBTTagCompound, tag, NBTTagString);
         tagField.set(NMSItemStack, NBTTagCompound);
         Object object1 = CraftItemStackClass.getMethod("asBukkitCopy", NMSItemStack.getClass()).invoke((Object)null, NMSItemStack);
         return (ItemStack)object1;
      } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | InstantiationException | IllegalAccessException var9) {
         var9.printStackTrace();
         return null;
      }
   }

   public static String getTag(ItemStack item, String tag) {
      try {
         Object NMSItemStack = CraftItemStackClass.getMethod("asNMSCopy", ItemStack.class).invoke((Object)null, item);
         Field tagField = NMSItemStack.getClass().getDeclaredField("tag");
         tagField.setAccessible(true);
         Object NBTTagCompound = tagField.get(NMSItemStack);
         if (NBTTagCompound == null) {
            return null;
         } else {
            Method getStringMethod = NBTTagCompound.getClass().getMethod("getString", String.class);
            getStringMethod.setAccessible(true);
            return (String)getStringMethod.invoke(NBTTagCompound, tag);
         }
      } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalAccessException var6) {
         var6.printStackTrace();
         return null;
      }
   }

   public static boolean hasTag(ItemStack item, String tag) {
      String data = getTag(item, tag);
      return data != null && data != "";
   }

   public static boolean hasTag(ItemStack item, String tag, String searchData) {
      String data = getTag(item, tag);
      return data != null && data.equals(searchData);
   }
}
