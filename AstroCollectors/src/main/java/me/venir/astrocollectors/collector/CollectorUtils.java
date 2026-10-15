package me.venir.astrocollectors.collector;

import de.tr7zw.nbtapi.NBTItem;
import me.venir.astrocollectors.utils.Utils;
import me.venir.astrocollectors.utils.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CollectorUtils {
    private CollectorUtils() { }

    public static ItemStack createCollector(int amount) {
        ItemStack itemStack = ItemBuilder.buildItem(Material.BEACON, amount, (short) 0,
                Utils.format("&a&lChunk Collector"),
                Utils.format("&7This collector will automatically pick up"),
                Utils.format("&7mob loot in the same chunk"),
                Utils.format(""),
                Utils.format("&aUsage: &fPlace down in your claims"));
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("astroCustomItem", "chunkCollector");
        return nbtItem.getItem();
    }

    public static boolean isCollector(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return false;
        if (itemStack.getType() != Material.BEACON) return false;
        NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey("astroCustomItem")) return false;
        return nbtItem.getString("astroCustomItem").equalsIgnoreCase("chunkCollector");
    }
}