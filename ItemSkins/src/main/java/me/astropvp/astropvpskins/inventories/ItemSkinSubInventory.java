package me.astropvp.astropvpskins.inventories;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.Arrays;

import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.enums.SkinType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemSkinSubInventory {
    public static void openInventory(Player player, ItemSkinType type) {
        String display = (type == ItemSkinType.HELMET) ? "Helmets" : ((type == ItemSkinType.BACKPACK) ? "Backpacks" : ((type == ItemSkinType.LEGGINGS) ? "Leggings" : ((type == ItemSkinType.BOOTS) ? "Boots" : ((type == ItemSkinType.SWORD) ? "Swords" : ((type == ItemSkinType.AXE) ? "Axes" : ((type == ItemSkinType.BOW) ? "Bows" : ((type == ItemSkinType.AMULET) ? "Amulets" : ((type == ItemSkinType.BELT) ? "Belts" : ((type == ItemSkinType.PICKAXE) ? "Pickaxes" : null)))))))));
        byte b1;
        int j;
        SkinType[] arrayOfSkinType1;

        Inventory inventory = Bukkit.createInventory(null, 54, CC.translate("&8Item Skins: ") + display);
        int pos = 0;
        byte b2 = 0;
        SkinType[] arrayOfSkinType2;
        for (SkinType skinType : SkinType.values()) {
            if (!skinType.isEnabled()) continue;
            if (ItemSkinType.isSimilarItemSkin(skinType.getItemSkinType(), type)) {
                inventory.setItem(pos, skinType.getItem());
                pos++;
            }
            b2 = (byte)(b2 + 1);
        }
        inventory.setItem(inventory.getSize() - 1, createBackButton());
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i, ItemSkinsInventory.createPane(7));
        }
        player.openInventory(inventory);
    }

    public static ItemStack createBackButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.RedB + "<- Go Back");
        meta.setLore(Arrays.asList(CC.Gray + "Click to return to", CC.Gray + "the main skins menu."));
        item.setItemMeta(meta);
        return item;
    }
}
