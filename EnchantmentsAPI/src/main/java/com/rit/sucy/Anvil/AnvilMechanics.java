/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package com.rit.sucy.Anvil;

import com.rit.sucy.Anvil.AnvilView;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilMechanics {
    private static final HashMap<String, Material> REPAIR_MATS = new HashMap<String, Material>(){
        {
            this.put("WOOD_", Material.WOOD);
            this.put("STONE_", Material.COBBLESTONE);
            this.put("CHAINMAIL_", Material.IRON_INGOT);
            this.put("IRON_", Material.IRON_INGOT);
            this.put("GOLD_", Material.GOLD_INGOT);
            this.put("DIAMOND_", Material.DIAMOND);
        }
    };

    public static void updateResult(AnvilView view, ItemStack[] input, String name) {
        if (input.length < 2) {
            view.setResultSlot(null);
            return;
        }
        ItemStack first = input[0];
        ItemStack second = input[1];
        if (AnvilMechanics.isFilled(first) && !AnvilMechanics.isFilled(second) && name != null) {
            ItemStack result = first.clone();
            ItemMeta meta = result.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(result.getType()) : result.getItemMeta();
            meta.setDisplayName(name);
            result.setItemMeta(meta);
            view.setResultSlot(result);
            int cost = AnvilMechanics.getBaseCost(first, false) + (first.getType().getMaxDurability() != 0 ? 7 : 5 * first.getAmount());
            view.setRepairCost(cost > 39 ? 39 : cost);
        } else if (!AnvilMechanics.isFilled(first) && AnvilMechanics.isFilled(second) && name != null) {
            ItemStack result = second.clone();
            ItemMeta meta = result.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(result.getType()) : result.getItemMeta();
            meta.setDisplayName(name);
            result.setItemMeta(meta);
            view.setResultSlot(result);
            int cost = AnvilMechanics.getBaseCost(first, false) + (first.getType().getMaxDurability() != 0 ? 7 : 5 * first.getAmount());
            view.setRepairCost(cost > 39 ? 39 : cost);
        } else if (!AnvilMechanics.isFilled(first) || !AnvilMechanics.isFilled(second)) {
            view.setResultSlot(null);
        } else if (first.getType() != second.getType()) {
            if (AnvilMechanics.isBook(first)) {
                AnvilMechanics.validateBook(view, first, second);
            } else if (AnvilMechanics.isBook(second)) {
                AnvilMechanics.validateBook(view, second, first);
            } else {
                for (Map.Entry<String, Material> entry : REPAIR_MATS.entrySet()) {
                    int cost;
                    if (first.getType().name().contains(entry.getKey()) && second.getType() == entry.getValue()) {
                        cost = AnvilMechanics.getMaterialRepairCost(first, second) + AnvilMechanics.getBaseCost(first, false);
                        view.setResultSlot(AnvilMechanics.makeItem(first, second.getAmount()));
                        view.setRepairCost(cost);
                        continue;
                    }
                    if (!second.getType().name().contains(entry.getKey()) || first.getType() != entry.getValue()) continue;
                    cost = AnvilMechanics.getMaterialRepairCost(second, first) + AnvilMechanics.getBaseCost(second, false);
                    view.setResultSlot(AnvilMechanics.makeItem(second, first.getAmount()));
                    view.setRepairCost(cost);
                }
            }
        } else if (first.getAmount() > 1) {
            view.setResultSlot(null);
        } else if (second.getAmount() > 1) {
            view.setResultSlot(null);
        } else if (EnchantmentAPI.getAllEnchantments(first).size() * EnchantmentAPI.getAllEnchantments(second).size() > 0) {
            view.setResultSlot(AnvilMechanics.makeItem(first, second));
            view.setRepairCost(AnvilMechanics.getCombineCost(first, second));
        } else if (first.getDurability() > 0) {
            view.setResultSlot(AnvilMechanics.makeItem(first, second));
            view.setRepairCost(AnvilMechanics.getCombineCost(first, second));
        } else {
            view.setResultSlot(null);
        }
    }

    public static void updateResult(AnvilView view, ItemStack[] input) {
        AnvilMechanics.updateResult(view, input, null);
    }

    private static int getBaseCost(ItemStack item, boolean withBook) {
        int cost = 0;
        int count = 0;
        for (Map.Entry<CustomEnchantment, Integer> entry : EnchantmentAPI.getAllEnchantments(item).entrySet()) {
            cost += entry.getKey().getCostPerLevel(withBook) * entry.getValue();
            ++count;
        }
        return cost + (int)((double)(count + 1) * ((double)count / 2.0) + 0.5);
    }

    private static int getMaterialRepairCost(ItemStack item, ItemStack material) {
        int m = material.getType() == Material.DIAMOND ? 3 : 1;
        int cost = 4 * m * item.getDurability() / item.getType().getMaxDurability() + 1;
        if (cost > m * material.getAmount()) {
            cost = m * material.getAmount();
        }
        return cost;
    }

    private static int getCombineCost(ItemStack first, ItemStack second) {
        boolean book = AnvilMechanics.isBook(second);
        double m = book ? 0.5 : 1.0;
        int cost = 0;
        int extra = AnvilMechanics.getBaseCost(first, book);
        if (first.getDurability() > 0 && first.getType() == second.getType()) {
            int extraCost = (int)(((double)AnvilMechanics.durability(second) + 0.12 * (double)first.getType().getMaxDurability()) / 100.0);
            cost += extraCost > 0 ? extraCost : 1;
        }
        Set<Map.Entry<CustomEnchantment, Integer>> enchants = EnchantmentAPI.getAllEnchantments(first).entrySet();
        int newEnchants = 0;
        for (Map.Entry<CustomEnchantment, Integer> entry : EnchantmentAPI.getAllEnchantments(second).entrySet()) {
            boolean conflict = false;
            boolean needsCost = false;
            if (entry.getKey().canEnchantOnto(first)) {
                for (Map.Entry<CustomEnchantment, Integer> e : enchants) {
                    if (e.getKey().name().equals(entry.getKey().name())) {
                        if (e.getValue() < entry.getValue()) {
                            cost += (entry.getValue() - e.getValue()) * entry.getKey().getCostPerLevel(book);
                            extra += (entry.getValue() - e.getValue()) * entry.getKey().getCostPerLevel(book);
                        } else if (e.getValue().equals(entry.getValue())) {
                            cost += entry.getKey().getCostPerLevel(false);
                            extra += entry.getKey().getCostPerLevel(false);
                        } else {
                            needsCost = true;
                        }
                        conflict = true;
                        continue;
                    }
                    if (!e.getKey().conflictsWith(entry.getKey())) continue;
                    conflict = true;
                    needsCost = true;
                }
            } else {
                needsCost = true;
                conflict = true;
            }
            if (!conflict) {
                ++newEnchants;
                cost += entry.getValue() * entry.getKey().getCostPerLevel(book);
                extra += entry.getValue() * entry.getKey().getCostPerLevel(book);
            }
            if (!needsCost) continue;
            cost += entry.getValue().intValue();
        }
        if (newEnchants > 0) {
            extra += newEnchants * (enchants.size() + newEnchants - 1) + 1;
        }
        return cost + (int)(m * (double)extra);
    }

    static void validateBook(AnvilView inv, ItemStack book, ItemStack target) {
        Map<CustomEnchantment, Integer> enchants = EnchantmentAPI.getAllEnchantments(book);
        boolean valid = false;
        if (target.getType() != Material.BOOK && target.getType() != Material.ENCHANTED_BOOK) {
            for (CustomEnchantment e : enchants.keySet()) {
                if (!e.canEnchantOnto(target)) continue;
                valid = true;
            }
        }
        if (valid && enchants.size() > 0 && book.getAmount() == 1 && target.getAmount() == 1) {
            inv.setResultSlot(AnvilMechanics.makeItem(target, book));
            inv.setRepairCost(AnvilMechanics.getCombineCost(target, book));
        } else {
            inv.setResultSlot(null);
        }
    }

    static ItemStack makeItem(ItemStack item, int amount) {
        ItemStack newItem = item.clone();
        if ((double)item.getDurability() - (double)item.getType().getMaxDurability() * 0.25 * (double)amount < 0.0) {
            newItem.setDurability((short)0);
        } else {
            newItem.setDurability((short)((double)item.getDurability() - (double)(item.getType().getMaxDurability() * amount) * 0.25));
        }
        return newItem;
    }

    static ItemStack makeItem(ItemStack primary, ItemStack secondary) {
        ItemStack item = new ItemStack(primary.getType());
        if (primary.hasItemMeta()) {
            item.setItemMeta(primary.getItemMeta());
        }
        if (primary.getDurability() > 0 && primary.getType() == secondary.getType()) {
            if ((double)(AnvilMechanics.durability(item) + AnvilMechanics.durability(secondary)) < 0.88 * (double)primary.getType().getMaxDurability()) {
                AnvilMechanics.setDurability(item, (short)((double)(AnvilMechanics.durability(primary) + AnvilMechanics.durability(secondary)) + 0.12 * (double)primary.getType().getMaxDurability()));
            }
        } else {
            item.setDurability(primary.getDurability());
        }
        Set<Map.Entry<CustomEnchantment, Integer>> enchants = EnchantmentAPI.getAllEnchantments(primary).entrySet();
        for (Map.Entry<CustomEnchantment, Integer> entry : EnchantmentAPI.getAllEnchantments(secondary).entrySet()) {
            boolean conflict = false;
            if (entry.getKey().canEnchantOnto(item)) {
                for (Map.Entry<CustomEnchantment, Integer> e : enchants) {
                    if (e.getKey().name().equals(entry.getKey().name())) {
                        e.getKey().removeFromItem(item);
                        int level = e.getValue();
                        if (entry.getValue() > level) {
                            level = entry.getValue();
                        } else if (entry.getValue() == level) {
                            level = Math.min(level + 1, e.getKey().getMaxLevel());
                        }
                        e.getKey().addToItem(item, level);
                        conflict = true;
                        continue;
                    }
                    if (!e.getKey().conflictsWith(entry.getKey())) continue;
                    conflict = true;
                }
            } else {
                conflict = true;
            }
            if (conflict) continue;
            entry.getKey().addToItem(item, entry.getValue());
        }
        return item;
    }

    static boolean isFilled(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    static boolean isBook(ItemStack item) {
        return item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK;
    }

    static short durability(ItemStack item) {
        return (short)(item.getType().getMaxDurability() - item.getDurability());
    }

    static ItemStack setDurability(ItemStack item, short value) {
        item.setDurability((short)(item.getType().getMaxDurability() - value));
        return item;
    }
}

