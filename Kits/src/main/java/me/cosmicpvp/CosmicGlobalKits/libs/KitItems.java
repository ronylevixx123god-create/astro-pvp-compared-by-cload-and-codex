package me.cosmicpvp.CosmicGlobalKits.libs;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
public class KitItems {
    public static ItemStack getKitItem(GlobalKits kit) {
        return ItemUtils.buildItem(Material.DIAMOND, (short)0, ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "KIT \"" + (Object)ChatColor.GOLD + kit.getItemName() + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "\"", (Object)ChatColor.GRAY + "Right-click this gem to unlock ", (Object)ChatColor.GRAY + "the " + kit.getItemName() + (Object)ChatColor.GRAY + " /gkit on this planet.", "", (Object)ChatColor.GRAY + "If you already have this /gkit unlocked,", (Object)ChatColor.GRAY + "you can redeem this gem to increase", (Object)ChatColor.GRAY + "the " + CC.YellowB + "Kit Tier" + CC.Gray + " which will increase", (Object)ChatColor.GRAY + "the enchantment levels on the /gkit", (Object)ChatColor.GRAY + "items each time you claim it.");
    }

    public static GlobalKits getKitFromItemstack(ItemStack i) {
        if (i.hasItemMeta() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().startsWith(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "KIT ")) {
            String gkitName = KitItems.getKitItemName(i);
            for (GlobalKits gkit : GlobalKits.values()) {
                String cleanedName = ChatColor.stripColor((String)gkit.getItemName()).replace("!!!Heroic!!! ", "");
                if (cleanedName.equals(gkitName)) {
                    return gkit;
                }
                if (!gkitName.contains("Butcher")) continue;
                return GlobalKits.Butcher;
            }
        }
        return null;
    }

    private static String getKitItemName(ItemStack i) {
        return ChatColor.stripColor((String)i.getItemMeta().getDisplayName().replace(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "KIT \"" + (Object)ChatColor.GOLD, "").replace(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "\"", "")).replace("!!!Heroic!!! ", "");
    }
}