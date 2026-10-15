package me.ifamasssxd.cosmicmysterychest.halflootbox;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HalfLootbox {
    private String lootboxInternalName;
    private HalfLootboxType halfLootboxType;
    private Material material;
    private String displayName;

    HalfLootbox(String lootboxInternalName, HalfLootboxType halfLootboxType) {
        this.lootboxInternalName = lootboxInternalName;
        this.halfLootboxType = halfLootboxType;
        this.material = Material.PRISMARINE_CRYSTALS;
        this.displayName = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(lootboxInternalName).getDisplayName();

    }

    public String getLootboxInternalName() {
        return lootboxInternalName;
    }

    public void setLootboxInternalName(String lootboxInternalName) {
        this.lootboxInternalName = lootboxInternalName;
    }

    public HalfLootboxType getHalfLootboxType() {
        return halfLootboxType;
    }

    public void setHalfLootboxType(HalfLootboxType halfLootboxType) {
        this.halfLootboxType = halfLootboxType;
    }

    public HalfLootbox getHalfLootboxFromItem(ItemStack item) {
        //TODO
        return null;
    }


    public ItemStack getHalfLootboxItem() {
        ItemStack halfLootboxItem = new ItemStack(material);
        ItemMeta meta = halfLootboxItem.getItemMeta();
        List<String> lore = new ArrayList<String>();

        if (halfLootboxType == HalfLootboxType.LEFT) {
            meta.setDisplayName(CC.translate("&b&l" + halfLootboxType.getHalfLootboxString() + " Half (" + displayName + "&b&l)"));
            lore.add(CC.translate("&7Drag 'n Drop this &b&l" + halfLootboxType.getHalfLootboxString() + " Half&7 onto a &d&lRight Half"));
            lore.add(CC.translate("&7to combine them and unlock the full lootbox."));
            lore.add("");
            lore.add(CC.translate("&f&lTYPE: &b" + getHalfLootboxType().getHalfLootboxString()));
        } else {
            meta.setDisplayName(CC.translate("&d&l" + halfLootboxType.getHalfLootboxString() + " Half (" + displayName + "&d&l)"));
            lore.add(CC.translate("&7Drag 'n Drop this &d&l" + halfLootboxType.getHalfLootboxString() + " Half&7 onto a &b&lLeft Half"));
            lore.add(CC.translate("&7to combine them and unlock the full lootbox."));
            lore.add("");
            lore.add(CC.translate("&f&lTYPE: &d" + getHalfLootboxType().getHalfLootboxString()));
        }
        meta.setLore(lore);
        halfLootboxItem.setItemMeta(meta);
        halfLootboxItem = addGlow(halfLootboxItem);

        NBTWrapper wrapper = new NBTWrapper(halfLootboxItem);
        wrapper.setString("cosmicType", getHalfLootboxType().getHalfLootboxString() + "_halflootbox");
        wrapper.setString("lootbox_name", lootboxInternalName);
        return wrapper.build();
    }

    private ItemStack addGlow(ItemStack item) {
        NBTTagCompound tag;
        net.minecraft.server.v1_8_R3.ItemStack nmsStack;
        block3:
        {
            block2:
            {
                nmsStack = CraftItemStack.asNMSCopy((ItemStack) item);
                tag = null;
                if (nmsStack.hasTag()) break block2;
                tag = new NBTTagCompound();
                nmsStack.setTag(tag);
                if (tag != null) break block3;
            }
            tag = nmsStack.getTag();
        }
        NBTTagList ench = new NBTTagList();
        tag.set("ench", (NBTBase) ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror((net.minecraft.server.v1_8_R3.ItemStack) nmsStack);
    }


    public static boolean isHalfLootboxItem(ItemStack item) {
        if (item.getType() != Material.PRISMARINE_CRYSTALS || !item.hasItemMeta())
            return false;
        return new NBTWrapper(item).getString("cosmicType").contains("_halflootbox");
    }

    public static HalfLootboxType getHalfLootboxType(ItemStack item) {
        if (item.getType() != Material.PRISMARINE_CRYSTALS || !item.hasItemMeta())
            return null;
        String nbtString = new NBTWrapper(item).getString("cosmicType");
        nbtString = nbtString.replace("_halflootbox", "").toUpperCase();
        HalfLootboxType halfLootboxType = HalfLootboxType.valueOf(nbtString);
        return halfLootboxType;
    }
}


