package me.ifamasssxd.cosmicmysterychest.utils;

import me.ifamasssxd.cosmicmysterychest.struct.CustomLoot;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FilterUtils {
    public static List<ItemStack> getItems(MysteryChest chest) {
        List<ItemStack> list = new ArrayList<>(chest.getGarunteedItems());
        for(CustomLoot item : chest.getLootItems()) {
            list.add(item.getItem());
        }
        return list;
    }
}
