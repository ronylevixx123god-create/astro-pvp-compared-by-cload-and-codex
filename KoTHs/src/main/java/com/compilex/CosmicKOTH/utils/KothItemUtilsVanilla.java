/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.itembuilder.EssentialsItemBuilder
 *  com.google.common.collect.Lists
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.compilex.CosmicKOTH.utils;

import com.cosmicpvp.cosmicutils.itembuilder.EssentialsItemBuilder;
import com.google.common.collect.Lists;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KothItemUtilsVanilla {
    private static SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yy");

    public static ItemStack getRandomKothItem(Player kothWinner) {
        int i = new Random().nextInt(10);
        if (i == 0) {
            return KothItemUtilsVanilla.getKothHelmet(kothWinner);
        }
        if (i == 1) {
            return KothItemUtilsVanilla.getKothChestplate(kothWinner);
        }
        if (i == 2) {
            return KothItemUtilsVanilla.getKothLeggings(kothWinner);
        }
        if (i == 3) {
            return KothItemUtilsVanilla.getKothBoots(kothWinner);
        }
        if (i == 4) {
            return KothItemUtilsVanilla.getKothAxe(kothWinner);
        }
        if (i == 5) {
            return KothItemUtilsVanilla.getKothSwordPvP(kothWinner);
        }
        if (i == 6) {
            return KothItemUtilsVanilla.getKothSwordPvE(kothWinner);
        }
        if (i == 7) {
            return KothItemUtilsVanilla.getKothBow(kothWinner);
        }
        if (i == 8) {
            return KothItemUtilsVanilla.getKothSwordPvPDerp(kothWinner);
        }
        if (i == 9) {
            return KothItemUtilsVanilla.getKothPickaxe(kothWinner);
        }
        return null;
    }

    public static List<ItemStack> getArcherSet() {
        return Lists.newArrayList(new EssentialsItemBuilder("298 1 protection:5 unbreaking:4 respiration:3 name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Archer_Helmet lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER} color:0,0,0").build(), new EssentialsItemBuilder("299 1 protection:4 unbreaking:4 name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Archer_Chestplate lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER} color:0,0,0").build(), new EssentialsItemBuilder("300 1 protection:5 unbreaking:4 name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Archer_Leggings lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER} color:0,0,0").build(), new EssentialsItemBuilder("301 1 protection:5 unbreaking:4 featherfalling:4 depthstrider:3 name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Archer_Boots lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER} color:0,0,0").build());
    }

    public static List<ItemStack> getBardSet() {
        return Lists.newArrayList(new EssentialsItemBuilder("314 1 protection:5 unbreaking:4 respiration:3 name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Bard_Helmet lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER}").build(), new EssentialsItemBuilder("315 1 protection:5 unbreaking:4  name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Bard_Chestplate lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER}").build(), new EssentialsItemBuilder("316 1 protection:5 unbreaking:4  name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Bard_Leggings lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER}").build(), new EssentialsItemBuilder("317 1 protection:5 unbreaking:4 featherfalling:4 depthstrider:3  name:&f&k\u2019_&c&lK&6&l.&e&lO&a&lT&b&l.&d&lH_&f&k;_&6&lMaxed_Bard_Boots lore:&6&oUnlocked:_&f{DATE}|&6&oBy:_&f{PLAYER}").build());
    }

    public static ItemStack getKothHelmet(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_HELMET, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nHelmet&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        kothItem.addUnsafeEnchantment(Enchantment.OXYGEN, 3);
        kothItem.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
        return kothItem;
    }

    public static ItemStack getKothChestplate(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_CHESTPLATE, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nChestplate&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothLeggings(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_LEGGINGS, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nLeggings&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothBoots(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_BOOTS, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nBoots&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothAxe(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_AXE, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nAxe&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 4);
        kothItem.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothSwordPvP(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_SWORD, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nPvP Sword&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        kothItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothSwordPvPDerp(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_SWORD, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nPvP Derp Sword&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        kothItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
        kothItem.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothSwordPvE(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_SWORD, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nGrind Sword&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 5);
        kothItem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
        kothItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothBow(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.BOW, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nBow&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 6);
        kothItem.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 2);
        kothItem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }

    public static ItemStack getKothPickaxe(Player kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_PICKAXE, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nPickaxe&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner.getName() + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
        kothItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        return kothItem;
    }
}

