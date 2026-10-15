/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.ifamasssxd.cosmicmasks.items;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultiMaskItem
extends CosmicItem {
    public MaskType multiMask = MaskType.MULTI_MASK;

    public MultiMaskItem(String internal) {
        super(internal);
    }

    public ItemStack createItem(Player player, Object ... objects) {
        if (objects == null || objects.length < 1) {
            return null;
        }
        ArrayList<MaskType> masks = new ArrayList<MaskType>();
        for (int i = 0; i < objects.length; ++i) {
            MaskType maskType = MaskType.valueOf(String.valueOf(objects[i]).toUpperCase());
            masks.add(maskType);
        }
        ItemStack baseMask = this.multiMask.createMask(true);
        NBTWrapper nbtWrapper = new NBTWrapper(baseMask);
        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder(nbtWrapper.getNBTBase("conaxData"));
        builder.setString("multiMask", MultiMaskItem.listToString(masks));
        baseMask = nbtWrapper.setNBTBase("conaxData", (NBTBase)builder.get()).build();
        ItemMeta im = baseMask.getItemMeta();
        String equippedMasks = "";
        for (MaskType mask : masks) {
            equippedMasks = equippedMasks + mask.getMaskData().getDisplayName() + ", ";
        }
        equippedMasks = equippedMasks.substring(0, equippedMasks.length() - 2);
        im.setDisplayName(CC.WhiteB + "Multi-Mask (" + CC.Reset + equippedMasks + CC.WhiteB + ")");
        LinkedList<String> lore = new LinkedList<String>();
        lore.add(CC.Gray + "This mask contains the powers of:");
        for (MaskType mask2 : masks) {
            lore.add(CC.WhiteB + "* " + mask2.getMaskData().getDisplayName());
            lore.add(CC.WhiteB + "(" + mask2.getMaskData().getSpecialAbility() + CC.WhiteB + ")");
        }
        im.setLore(lore);
        baseMask.setItemMeta(im);
        return baseMask;
    }

    public static List<MaskType> getMasks(NBTWrapper multiMaskNBT) {
        NBTTagCompoundBuilder builder;
        ArrayList<MaskType> masks = new ArrayList<MaskType>();
        NBTWrapper nbtWrapper = multiMaskNBT;
        if (nbtWrapper != null && (builder = new NBTTagCompoundBuilder(nbtWrapper.getNBTBase("conaxData"))) != null) {
            for (String maskName : builder.getString("multiMask").split(",")) {
                masks.add(MaskType.valueOf(maskName));
            }
        }
        return masks;
    }

    public static String listToString(List<MaskType> masks) {
        String r = "";
        for (MaskType type : masks) {
            r = r + type.name() + ",";
        }
        return r.substring(0, r.length() - 1);
    }
}

