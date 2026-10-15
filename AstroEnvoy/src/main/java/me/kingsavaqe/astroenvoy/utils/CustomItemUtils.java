package me.kingsavaqe.astroenvoy.utils;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItemUtils {

    public static boolean isCrankedCustomItem(ItemStack itemStack) {
        if(itemStack == null) return false;
        if (itemStack.getType() == Material.AIR) return false;

        NBTItem nbtItem = new NBTItem(itemStack);
        if(!nbtItem.hasNBTData()) return false;
        return nbtItem.hasKey("crankedCustomItem");
    }

    public static String getCrankedCustomItem(ItemStack itemStack) {
        if(itemStack == null) return "";
        if (itemStack.getType() == Material.AIR) return "";
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.getString("crankedCustomItem");
    }


    public static ItemStack setCrankedCustomItem(ItemStack itemStack, String string) {
        if(itemStack == null) return itemStack;
        if (itemStack.getType() == Material.AIR) return itemStack;

        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("crankedCustomItem", string);
        return nbtItem.getItem();
    }

}
