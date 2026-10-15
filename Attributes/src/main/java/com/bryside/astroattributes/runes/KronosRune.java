package com.bryside.astroattributes.runes;

import com.bryside.astroattributes.objects.Rune;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KronosRune extends Rune implements Listener {
    HashMap atlases = Maps.newHashMap();
    HashMap hits = Maps.newHashMap();

    public String[] getLore(int level, int chance) {
        if (level > this.getMaxLevel()) {
            level = this.getMaxLevel();
        }

        return new String[]{CC.RedB + "Level: " + CC.DarkGray + level, CC.RedB + "Apply: " + CC.DarkGray + chance + "%", "", CC.RedB + "''Rage Rampage''", CC.Red + "Rage stacks do NOT fully clear", CC.Red + "when you take damage. The % that", CC.Red + "DO clear decreases per level.", "", CC.Gray + "Titan Attributes can " + CC.Underline + "ONLY" + CC.Gray + " be applied", CC.Gray + "to " + CC.Red + "Heroic Weapons " + CC.Gray + "and only", CC.Gray + CC.Underline + "1 attribute" + CC.Gray + " can be imbued per weapon.", "", CC.Gray + "To learn more about Titan Attributes, use " + CC.Red + "/titan"};
    }

    public String getDisplayName() {
        return CC.RedB + "Titan Attribute (" + CC.DarkGrayB + "Kronos" + CC.RedB + ")";
    }

    public Material getMaterial() {
        return Material.SPIDER_EYE;
    }

    public ItemStack getItemStack(int level, int success) {
        ItemStack item = new ItemStack(this.getMaterial());
        ItemMeta itemMeta = item.getItemMeta();
        List lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList();
        String[] lore2;
        int length = (lore2 = this.getLore(level, success)).length;

        for (int i = 0; i < length; ++i) {
            String l = lore2[i];
            ((List) lore).add(l);
        }

        itemMeta.setLore((List) lore);
        itemMeta.setDisplayName(this.getDisplayName());
        item.setItemMeta(itemMeta);
        return item;
    }

    public void setNeededHits(Player player, Integer integer) {
        this.hits.put(player, integer);
    }


}

