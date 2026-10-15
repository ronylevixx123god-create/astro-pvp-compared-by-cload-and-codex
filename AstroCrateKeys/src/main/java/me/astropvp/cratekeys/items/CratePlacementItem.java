package me.astropvp.cratekeys.items;
import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;

import me.astropvp.cratekeys.enums.CrateType;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
public class CratePlacementItem {
    public static ItemStack getItem(CrateType type, Material material) {
        if (type == null)
            return null;
        ItemBuilder item = new ItemBuilder(material);
        item.setName(String.valueOf(type.getPrefixedName()) + " Crate");
        item.addLoreLine(String.valueOf(CC.Gray) + "Right-Click to place crate.");
        ItemStack built = item.build();
        NBTWrapper wrapper = new NBTWrapper(built);
        NBTTagCompoundBuilder tag = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) :
                new NBTTagCompoundBuilder();
        tag.setString("crate_place_type", type.name());
        wrapper.setNBTBase("cosmicData", (NBTBase)tag.get());
        return wrapper.build();
    }

    public static CrateType getCrateType(ItemStack item) {
        if (item == null || item.getType() == Material.AIR)
            return null;
        NBTWrapper wrapper = new NBTWrapper(item);
        if (!wrapper.hasKey("cosmicData"))
            return null;
        NBTTagCompoundBuilder tag = new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"));
        if (!tag.hasKey("crate_place_type"))
            return null;
        return CrateType.valueOf(tag.getString("crate_place_type"));
    }
}
