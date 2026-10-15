package me.kingsavaqe.astrotournaments.utils;
import com.rit.sucy.CustomEnchantment;
import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

public class ItemUtil {
    private static final Class<?> CraftItemStackClass = NMSUtil.getCraftClass("inventory.CraftItemStack");

    private static final Class<?> ItemStackClass = NMSUtil.getNMSClass("ItemStack");

    private static final Class<?> NBTTagCompoundClass = NMSUtil.getNMSClass("NBTTagCompound");

    public static String getItemName(ItemStack is){
        try {
            Object NMSItemStack = CraftItemStackClass.getMethod("asNMSCopy", new Class[]{ItemStack.class}).invoke(null, is);
            return (String) NMSItemStack.getClass().getMethod("getName", new Class[0]).invoke(NMSItemStack, new Object[0]);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e){
            e.printStackTrace();
            return WordUtils.capitalize(is.getType().name().replace("_", " ").toLowerCase());
        }
    }

    public static String convertItemStackToJson(ItemStack itemStack){
        try {
            assert CraftItemStackClass != null;
            Method asNMSCopyMethod = CraftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            Method saveMethod = ItemStackClass.getMethod("save", NBTTagCompoundClass);
            assert NBTTagCompoundClass != null;
            Object NBTTagCompound = NBTTagCompoundClass.newInstance();
            Object NMSItemStack = asNMSCopyMethod.invoke(null, itemStack);
            Object Json = saveMethod.invoke(NMSItemStack, NBTTagCompound);
            return Json.toString();
        } catch (IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e){
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack applyCustomEnchantments(ItemStack i, List<CustomEnchantment> enchants) {
        for (CustomEnchantment ce : enchants) {
            if (ce == null) continue;
            if (ce.heroicEnchantment) {
                ce.nonHeroicVersion.addToItem(i, ce.nonHeroicVersion.getMaxLevel());
                ce.addToItem(i, ce.getMaxLevel());
                continue;
            }
            ce.addToItem(i, ce.getMaxLevel());
        }
        return i;
    }
}