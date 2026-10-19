package net.crankedpvp.slotbot;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Reads the "crankedCustomItem" NBT tag that AstroSlotBot writes on its tickets (see SlotBotType).
 * SlotBotAPI referenced this class but it was never committed; recreated from the identical helper
 * in AstroEnvoy (me.kingsavaqe.astroenvoy.utils.CustomItemUtils).
 */
public class CustomItemUtils {

    public static String getCrankedCustomItem(ItemStack itemStack) {
        if (itemStack == null) return "";
        if (itemStack.getType() == Material.AIR) return "";
        String value = new NBTItem(itemStack).getString("crankedCustomItem");
        return value == null ? "" : value;
    }
}
