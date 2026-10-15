package net.astropvp.armorsets.utils.modifiers;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import net.astropvp.armorsets.utils.Pair;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AttributeModifier {

    public static ItemStack applyModifications(ItemStack itemStack, List<Pair<ModifierType, Double>> modifierTypePairs) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
            nmsStack.setTag(compound);
            compound = nmsStack.getTag();
        }

        NBTTagList modifiers = new NBTTagList();

        for(Pair<ModifierType, Double> modifierTypePair : modifierTypePairs) {
            ModifierType modifierType = modifierTypePair.getKey();
            Double amount = modifierTypePair.getValue();

            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.set("AttributeName", new NBTTagString(modifierType.getModifier()));
            nbtTagCompound.set("Name", new NBTTagString(modifierType.getModifier()));
            nbtTagCompound.set("Amount", new NBTTagDouble(amount));
            nbtTagCompound.set("Operation", new NBTTagInt(0));
            nbtTagCompound.set("UUIDLeast", new NBTTagInt(894654));
            nbtTagCompound.set("UUIDMost", new NBTTagInt(2872));

            modifiers.add(nbtTagCompound);
        }
        compound.set("AttributeModifiers", modifiers);
        nmsStack.setTag(compound);
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static double getModificationAmount(ItemStack itemStack, ModifierType modifierType) {
        NBTItem nbtItem = new NBTItem(itemStack);
        NBTCompound nbtCompound = nbtItem.getCompound("AttributeModifiers");
        return nbtCompound.getDouble("Amount");
    }

}
