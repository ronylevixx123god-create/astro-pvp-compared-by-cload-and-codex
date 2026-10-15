package com.conaxgames.customskulls.util;

import me.astropvp.cratekeys.AstroCrateKeysAPI;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class CrateKeyAPI {
    public static HashMap<CrateType, Integer> getCrateKeysInInventory(Inventory inventory) {
        HashMap<CrateType, Integer> keys = new HashMap<>();

        for (ItemStack itemStack : inventory) {
            if (AstroCrateKeysAPI.isCrateKey(itemStack)) {
                CrateType crateType = CrateKeyItem.getCrateType(itemStack);
                if (keys.containsKey(crateType)) {
                    int amount = keys.get(crateType);
                    keys.put(crateType, amount + itemStack.getAmount());
                } else {
                    keys.put(crateType, itemStack.getAmount());
                }
            }
        }
        return keys;
    }
}
