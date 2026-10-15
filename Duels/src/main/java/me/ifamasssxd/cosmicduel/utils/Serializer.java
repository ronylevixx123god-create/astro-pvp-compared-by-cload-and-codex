/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicduel.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.ifamasssxd.cosmicduel.utils.DuelUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Serializer {
    public static final String KIT_CONTENT_SEPERATOR = "@";
    public static final String POTION_SPLIT = "%";
    public static final String ITEM_SPLIT = "&";

    public static String getSerializedItemList(List<ItemStack> kitItems) {
        StringBuilder builder = new StringBuilder();
        HashMap<Short, Integer> potionCount = new HashMap<Short, Integer>();
        for (ItemStack itemStack : kitItems) {
            if (itemStack.getType() == Material.POTION) {
                if (potionCount.containsKey(itemStack.getDurability())) {
                    potionCount.put(itemStack.getDurability(), (Integer)potionCount.get(itemStack.getDurability()) + 1);
                    continue;
                }
                potionCount.put(itemStack.getDurability(), 1);
                continue;
            }
            if (DuelUtils.isWeapon(itemStack) || DuelUtils.isArmor(itemStack)) continue;
            builder.append(itemStack.getTypeId()).append(ITEM_SPLIT).append(itemStack.getAmount()).append(KIT_CONTENT_SEPERATOR);
        }
        for (Map.Entry entry : potionCount.entrySet()) {
            builder.append(entry.getKey()).append(POTION_SPLIT).append(entry.getValue()).append(KIT_CONTENT_SEPERATOR);
        }
        String retr = builder.toString();
        if (retr.endsWith(KIT_CONTENT_SEPERATOR)) {
            retr = retr.substring(0, retr.length() - 1);
        }
        return retr.replace("\\", "");
    }

    public static List<ItemStack> deserializeKitItemString(String kitType) {
        String[] split;
        ArrayList<ItemStack> retr = new ArrayList<ItemStack>();
        String[] content = split = kitType.split(KIT_CONTENT_SEPERATOR);
        for (String itemString : split) {
            int amount;
            if (itemString.contains(POTION_SPLIT)) {
                String[] potionData = itemString.split(POTION_SPLIT);
                int potionId = Integer.parseInt(potionData[0]);
                for (amount = Integer.parseInt(potionData[1]); amount > 0; --amount) {
                    retr.add(new ItemStack(Material.POTION, 1, (short)potionId));
                }
                continue;
            }
            if (!itemString.contains(ITEM_SPLIT)) continue;
            String[] itemData = itemString.split(ITEM_SPLIT);
            int id = Integer.parseInt(itemData[0]);
            amount = Integer.parseInt(itemData[1]);
            ItemStack is = new ItemStack(Material.getMaterial((int)id), amount);
            retr.add(is);
        }
        return retr;
    }
}

