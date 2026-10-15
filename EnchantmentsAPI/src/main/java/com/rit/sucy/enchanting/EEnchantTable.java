/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.permissions.Permissible
 */
package com.rit.sucy.enchanting;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.enchanting.EnchantResult;
import com.rit.sucy.service.MaterialClass;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

public class EEnchantTable {
    static final int MAX_TRIES = 10;

    public static EnchantResult enchant(Player enchanter, ItemStack item, int enchantLevel, int maxEnchants) {
        return EEnchantTable.enchant(enchanter, item, enchantLevel, maxEnchants, true);
    }

    public static EnchantResult enchant(Player enchanter, ItemStack item, int enchantLevel, int maxEnchants, boolean apply) {
        EnchantResult result = new EnchantResult();
        boolean chooseEnchantment = true;
        HashMap<CustomEnchantment, Integer> chosenEnchantWithCost = new HashMap<CustomEnchantment, Integer>();
        List<CustomEnchantment> validEnchants = enchanter != null ? EnchantmentAPI.getAllValidEnchants(item, (Permissible)enchanter) : EnchantmentAPI.getAllValidEnchants(item);
        int totalWeight = EEnchantTable.weightOfAllEnchants(validEnchants);
        int max = 0;
        while (chooseEnchantment && validEnchants.size() > 0) {
            CustomEnchantment enchant;
            chooseEnchantment = false;
            enchantLevel = EEnchantTable.modifiedLevel(enchantLevel, MaterialClass.getEnchantabilityFor(item.getType()));
            result.setLevel(enchantLevel);
            int tries = 0;
            int n = max = enchantLevel > max ? enchantLevel : max;
            do {
                int level;
                if ((level = (enchant = EEnchantTable.weightedRandom(validEnchants, totalWeight)).getEnchantLevel(max)) < 1) {
                    if (result.getFirstEnchant() == null) continue;
                    break;
                }
                chosenEnchantWithCost.put(enchant, level);
                totalWeight -= enchant.getWeight();
                validEnchants.remove(enchant);
                result.setFirstEnchant(enchant);
                break;
            } while (tries++ < 10 || result.getFirstEnchant() == null);
            if (Math.random() < (double)(enchantLevel + 1) / 50.0) {
                chooseEnchantment = true;
            }
            enchantLevel /= 2;
            if (item.getType() == Material.BOOK && chosenEnchantWithCost.size() == 2) {
                chooseEnchantment = false;
            }
            if (chosenEnchantWithCost.size() == maxEnchants) {
                chooseEnchantment = false;
            }
            if (!chooseEnchantment) continue;
            int prevWeight = totalWeight;
            for (int i = 0; i < validEnchants.size(); ++i) {
                if (!enchant.conflictsWith(validEnchants.get(i))) continue;
                totalWeight -= validEnchants.get(i).getWeight();
                validEnchants.remove(i--);
            }
            if (validEnchants.size() != 0) continue;
            chooseEnchantment = false;
        }
        result.setAddedEnchants(chosenEnchantWithCost);
        boolean success = false;
        if (apply) {
            for (Map.Entry enchantCostEntry : chosenEnchantWithCost.entrySet()) {
                CustomEnchantment selectedEnchant = (CustomEnchantment)enchantCostEntry.getKey();
                int levelCost = (Integer)enchantCostEntry.getValue();
                if (selectedEnchant == null) {
                    result.setItem(item);
                    return result;
                }
                selectedEnchant.addToItem(item, levelCost);
                success = true;
            }
        }
        if (success) {
            result.setItem(item);
        }
        return result;
    }

    static int modifiedLevel(int expLevel, int enchantability) {
        expLevel = expLevel + EEnchantTable.random(enchantability / 4 * 2) + 1;
        double bonus = EEnchantTable.random(0.3) + 0.85;
        return (int)((double)expLevel * bonus + 0.5);
    }

    static int random(int max) {
        return (int)(Math.random() * (double)max / 2.0 + Math.random() * (double)max / 2.0);
    }

    static double random(double max) {
        return Math.random() * max / 2.0 + Math.random() * max / 2.0;
    }

    static CustomEnchantment weightedRandom(Collection<CustomEnchantment> enchantments, int totalWeight) {
        int random = new Random().nextInt(totalWeight);
        Iterator<CustomEnchantment> iter = enchantments.iterator();
        CustomEnchantment enchantment = null;
        int current = 0;
        while (iter.hasNext()) {
            enchantment = iter.next();
            if (random >= (current += enchantment.getWeight())) continue;
            return enchantment;
        }
        return enchantment;
    }

    static int weightOfAllEnchants(Collection<CustomEnchantment> validEnchants) {
        int count = 0;
        for (CustomEnchantment enchantment : validEnchants) {
            count += enchantment.getWeight();
        }
        return count;
    }
}

