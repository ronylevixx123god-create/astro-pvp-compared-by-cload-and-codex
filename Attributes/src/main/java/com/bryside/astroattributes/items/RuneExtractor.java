package com.bryside.astroattributes.items;

import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RuneExtractor {
    public static Material material;
    public static String displayName;

    static {
        material = Material.BOWL;
        displayName = CC.translate("&d&lRune Extractor &7(Drag n' Drop)");
    }

    public static ItemStack createNewRuneExtractor(int apply) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        List lore = Arrays.asList(CC.Gray + "An exotic piece of scrap that has been", CC.Gray + "flying around in the cosmos for an eternity.", CC.Gray + "The few that are lucky enough to obtain this", CC.Gray + "item can extract any sword with a rune", CC.Gray + "applied onto it, but " + CC.Underline + "ONLY" + CC.Gray + " for a small price.", "", CC.LightPurple + CC.translate("Apply: &f") + apply + "%", CC.Red + "", CC.translate("&cPlace on sword to extract attribute."));
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(displayName);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static int getRuneExtractorSuccessRate(ItemStack item) {
        if (!isRuneExtractor(item)) {
            return -1;
        } else {
            Iterator var1 = item.getItemMeta().getLore().iterator();

            String l;
            do {
                if (!var1.hasNext()) {
                    return -1;
                }

                l = (String) var1.next();
            } while (!l.startsWith(CC.LightPurple + CC.translate("Apply: &f")));

            int apply = Integer.valueOf(l.substring(l.indexOf(":") + 4, l.indexOf("%")));
            return apply;
        }
    }

    public static boolean isRuneExtractor(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().hasDisplayName() && item.getType() == material) {
            ItemMeta itemMeta = item.getItemMeta();
            String display = itemMeta.getDisplayName();
            return display.contentEquals(displayName) && itemMeta.getLore().contains(CC.translate("&cPlace on sword to extract attribute."));
        } else {
            return false;
        }
    }

    public static Material getMaterial() {
        return material;
    }
}
