/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 */
package com.rit.sucy.enchanting;

import com.rit.sucy.service.ItemSets;
import com.rit.sucy.service.SuffixGroups;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public enum VanillaData {
    PROTECTION_ENVIRONMENTAL(Enchantment.PROTECTION_ENVIRONMENTAL, ItemSets.ARMOR.getItems(), "Damage_Reduction", new String[]{SuffixGroups.DEFENSE.getKey()}, 10, 1.0, 11.0, 4),
    PROTECTION_FALL(Enchantment.PROTECTION_FALL, ItemSets.BOOTS.getItems(), new String[]{SuffixGroups.DEFENSE.getKey(), SuffixGroups.FALL.getKey()}, 5, 5.0, 6.0, 4),
    PROTECTION_FIRE(Enchantment.PROTECTION_FIRE, ItemSets.ARMOR.getItems(), "Damage_Reduction", new String[]{SuffixGroups.DEFENSE.getKey(), SuffixGroups.FIRE.getKey()}, 5, 10.0, 8.0, 4),
    PROTECTION_PROJECTILE(Enchantment.PROTECTION_PROJECTILE, ItemSets.ARMOR.getItems(), "Damage_Reduction", new String[]{SuffixGroups.DEFENSE.getKey(), SuffixGroups.PROJECTILE.getKey()}, 5, 3.0, 6.0, 4),
    WATER_WORKER(Enchantment.WATER_WORKER, ItemSets.HELMETS.getItems(), new String[]{SuffixGroups.DIGGING.getKey(), SuffixGroups.BREATHING.getKey()}, 2, 1.0, 20.0, 1),
    PROTECTION_EXPLOSIONS(Enchantment.PROTECTION_EXPLOSIONS, ItemSets.ARMOR.getItems(), "Damage_Reduction", new String[]{SuffixGroups.DEFENSE.getKey(), SuffixGroups.EXPLOSIONS.getKey()}, 2, 5.0, 8.0, 4),
    OXYGEN(Enchantment.OXYGEN, ItemSets.HELMETS.getItems(), new String[]{SuffixGroups.BREATHING.getKey()}, 2, 10.0, 10.0, 3),
    THORNS(Enchantment.THORNS, ItemSets.CHESTPLATES.getItems(), new String[]{SuffixGroups.STRENGTH.getKey()}, 1, 10.0, 20.0, 3),
    DAMAGE_ALL(Enchantment.DAMAGE_ALL, ItemSets.SWORDS.getItems(), "Bonus_Damage", new String[]{SuffixGroups.STRENGTH.getKey()}, 10, 1.0, 11.0, 5),
    DAMAGE_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS, ItemSets.SWORDS.getItems(), "Bonus_Damage", new String[]{SuffixGroups.STRENGTH.getKey()}, 4, 5.0, 8.0, 5),
    KNOCKBACK(Enchantment.KNOCKBACK, ItemSets.SWORDS.getItems(), new String[]{SuffixGroups.FORCE.getKey()}, 8, 4.0, 20.0, 2),
    DAMAGE_UNDEAD(Enchantment.DAMAGE_UNDEAD, ItemSets.SWORDS.getItems(), "Bonus_Damage", new String[]{SuffixGroups.STRENGTH.getKey()}, 4, 5.0, 8.0, 5),
    FIRE_ASPECT(Enchantment.FIRE_ASPECT, ItemSets.SWORDS.getItems(), new String[]{SuffixGroups.FIRE.getKey()}, 2, 9.0, 20.0, 2),
    LOOT_BONUS_MOBS(Enchantment.LOOT_BONUS_MOBS, ItemSets.SWORDS.getItems(), new String[]{SuffixGroups.LOOT.getKey()}, 2, 14.0, 9.0, 3),
    DIG_SPEED(Enchantment.DIG_SPEED, ItemSets.TOOLS.getItems(), new String[]{SuffixGroups.DIGGING.getKey()}, 10, 1.0, 10.0, 5),
    DURABILITY(Enchantment.DURABILITY, ItemSets.TOOLS.getItems(), new String[]{SuffixGroups.DURABILITY.getKey()}, 5, 5.0, 8.0, 3),
    LOOT_BONUS_BLOCKS(Enchantment.LOOT_BONUS_BLOCKS, ItemSets.TOOLS.getItems(), "Block_Modifier", new String[]{SuffixGroups.DIGGING.getKey(), SuffixGroups.LOOT.getKey()}, 2, 15.0, 9.0, 3),
    SILK_TOUCH(Enchantment.SILK_TOUCH, ItemSets.TOOLS.getItems(), "Block_Modifier", new String[]{SuffixGroups.DIGGING.getKey()}, 1, 15.0, 20.0, 1),
    ARROW_DAMAGE(Enchantment.ARROW_DAMAGE, ItemSets.BOW.getItems(), new String[]{SuffixGroups.STRENGTH.getKey()}, 10, 1.0, 10.0, 5),
    ARROW_FIRE(Enchantment.ARROW_FIRE, ItemSets.BOW.getItems(), new String[]{SuffixGroups.FIRE.getKey()}, 2, 20.0, 20.0, 1),
    ARROW_KNOCKBACK(Enchantment.ARROW_KNOCKBACK, ItemSets.BOW.getItems(), new String[]{SuffixGroups.FORCE.getKey()}, 2, 12.0, 20.0, 2),
    ARROW_INFINITE(Enchantment.ARROW_INFINITE, ItemSets.BOW.getItems(), new String[]{SuffixGroups.PROJECTILE.getKey(), SuffixGroups.DURABILITY.getKey()}, 1, 20.0, 20.0, 1);

    private final Enchantment enchantment;
    private final String group;
    private final int enchantWeight;
    private final double base;
    private final int maxLevel;
    private final double interval;
    private final Material[] items;
    private final String[] suffixGroup;

    private VanillaData(Enchantment enchantment, Material[] items, String[] suffixGroup, int enchantWeight, double base, double interval, int max) {
        this(enchantment, items, "Default", suffixGroup, enchantWeight, base, interval, max);
    }

    private VanillaData(Enchantment enchantment, Material[] items, String group, String[] suffixGroup, int enchantWeight, double base, double interval, int max) {
        this.enchantment = enchantment;
        this.group = group;
        this.enchantWeight = enchantWeight;
        this.base = base;
        this.maxLevel = max;
        this.interval = interval;
        this.items = items;
        this.suffixGroup = suffixGroup;
    }

    public String[] getSuffixGroup() {
        return this.suffixGroup;
    }

    public Material[] getItems() {
        return this.items;
    }

    public double getBase() {
        return this.base;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public double getInterval() {
        return this.interval;
    }

    public int getEnchantWeight() {
        return this.enchantWeight;
    }

    public String getGroup() {
        return this.group;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }
}

