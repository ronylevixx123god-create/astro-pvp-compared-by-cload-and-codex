package me.ifamasssxd.cosmicmasks.utils;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicmasks.items.MultiMaskItem;
import me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MaskApplyUtils {
    public static ItemStack applyMask(ItemStack item, MaskType maskType) {
        NBTWrapper other = new NBTWrapper(item);
        String mask = new NBTTagCompoundBuilder(other.getNBTBase("conaxData")).getString("maskOverride");
        NBTWrapper wrapper = new NBTWrapper(new CustomItemBuilder("multi_mask",maskType).build());
        mask = new NBTTagCompoundBuilder(wrapper.getNBTBase("conaxData")).getString("maskType");
        MaskType type = MaskType.getFromName(mask.toUpperCase());
        if (type == null) {
            return null;
        }
         return type.applyMaskNBT(wrapper, other);
    }
}
