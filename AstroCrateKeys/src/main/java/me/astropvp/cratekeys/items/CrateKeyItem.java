package me.astropvp.cratekeys.items;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;

import me.astropvp.cratekeys.enums.CrateType;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CrateKeyItem {
    public static ItemStack getItem(CrateType type) {
        if (type == null)
            return null;
        ItemBuilder item = new ItemBuilder(Material.GOLD_NUGGET);
        item.setName(type.getPrefixedName());
        item.addLoreLine(CC.translate("&7Right-Click on a " + type.getPrefix() +type.getName() + " &7Crate to open."));
        item.addLoreLine("");
        item.addLoreLine(CC.translate(type.getPrefix() + CC.Bold +"(!) " + type.getPrefix() + "Type &f/warp crates " + type.getPrefix() + " to open this crate key."));
        ItemStack built = item.build();
        NBTWrapper wrapper = new NBTWrapper(built);
        NBTTagCompoundBuilder tag = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) :
                new NBTTagCompoundBuilder();
        tag.setString("crate_type", type.name());
        if (type.name().contains("MUTATED")) {
            tag.setString("tintColor", "-10237440");
        }
        wrapper.setNBTBase("cosmicData", tag.get());
        return addGlow(wrapper.build());
    }

    public static CrateType getCrateType(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return null;
        NBTWrapper wrapper = new NBTWrapper(item);
        if (!wrapper.hasKey("cosmicData"))
            return null;
        NBTTagCompoundBuilder tag = new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"));
        if (!tag.hasKey("crate_type"))
            return null;
        return CrateType.valueOf(tag.getString("crate_type"));
    }

    public static ItemStack addGlow(ItemStack item) {
        return new NBTWrapper(item).setGlowing().build();
    }
}
