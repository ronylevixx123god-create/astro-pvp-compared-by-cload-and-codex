package me.ifamasssxd.cosmicsky.inventorypets.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SkullUtils {



    public static ItemStack buildSkullNative(ItemStack item, String skullTexture) {
        if (skullTexture == null) throw new IllegalStateException("Skull Texture cannot be null!");

        net.minecraft.server.v1_8_R3.ItemStack nbtItem = CraftItemStack.asNMSCopy(item);

        if (!nbtItem.hasTag()) nbtItem.setTag(new NBTTagCompound());

        NBTTagCompound skullOwnerCompound = nbtItem.getTag().getCompound("SkullOwner");
        if (skullOwnerCompound == null) nbtItem.getTag().set("SkullOwner", skullOwnerCompound = new NBTTagCompound());

        String uuid = UUID.randomUUID().toString();
        skullOwnerCompound.setString("Id", uuid);

        NBTTagCompound propertiesCompound = new NBTTagCompound();

        NBTTagList texturesList = propertiesCompound.getList("textures", 10);

        NBTTagCompound valueCompound = new NBTTagCompound();
        valueCompound.setString("Value", skullTexture);
        texturesList.add(valueCompound);

        propertiesCompound.set("textures", texturesList);
        skullOwnerCompound.set("Properties", propertiesCompound);

        nbtItem.getTag().set("SkullOwner", skullOwnerCompound);

        return CraftItemStack.asBukkitCopy(nbtItem);
    }

    public static ItemStack buildSkullNative(String skullTexture) {
        return buildSkullNative(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), skullTexture);
    }
}