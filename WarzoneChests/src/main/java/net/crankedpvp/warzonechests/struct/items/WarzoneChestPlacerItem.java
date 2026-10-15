package net.crankedpvp.warzonechests.struct.items;

import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import de.tr7zw.nbtapi.NBTItem;
import net.crankedpvp.warzonechests.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WarzoneChestPlacerItem {

    private static final String WARZONE_CHEST_NBT = "warzoneChest";
    public static ItemStack getItem() {
        ItemStack warzoneChestPlacer = new ItemBuilder(Material.CHEST).setName("&4Warzone Chest Placer")
                .build();

        NBTItem nbtItem = new NBTItem(warzoneChestPlacer);
        nbtItem.setString("crankedCustomItem", WARZONE_CHEST_NBT);
        return nbtItem.getItem();
    }

    public static boolean isWarzoneChestPlace(ItemStack itemStack) {
        if (ItemUtils.isAirOrNull(itemStack)) return false;
        if (itemStack.getType() != Material.CHEST) return false;

        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasKey("crankedCustomItem")) return false;

        return nbtItem.getString("crankedCustomItem").equals(WARZONE_CHEST_NBT);
    }
}
