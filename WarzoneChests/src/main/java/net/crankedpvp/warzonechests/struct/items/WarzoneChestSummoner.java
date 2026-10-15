package net.crankedpvp.warzonechests.struct.items;

import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import de.tr7zw.nbtapi.NBTItem;
import net.crankedpvp.warzonechests.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WarzoneChestSummoner {

    private static final Material WARZONE_CHEST_SUMMONER_MATERIAL = Material.REDSTONE_TORCH_ON;
    private static final String WARZONE_CHEST_SUMMONER = "warzoneChestSummoner";

    public static ItemStack getItem() {
        ItemStack item = new ItemBuilder(WARZONE_CHEST_SUMMONER_MATERIAL)
            .setName("&c&lWarzone Chest Summoner")
            .setLore(
                "&7Right-Click this summoner to",
                "&7forcefully reset all your warzone chests"
            )
            .colorize()
            .build();

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("crankedCustomItem", WARZONE_CHEST_SUMMONER);
        return nbtItem.getItem();
    }

    public static boolean isWarzoneChestPlace(ItemStack itemStack) {
        if (ItemUtils.isAirOrNull(itemStack)) return false;
        if (itemStack.getType() != WARZONE_CHEST_SUMMONER_MATERIAL) return false;

        NBTItem nbtItem = new NBTItem(itemStack);

        if (!nbtItem.hasKey("crankedCustomItem")) return false;

        return nbtItem.getString("crankedCustomItem").equals(WARZONE_CHEST_SUMMONER);
    }
}