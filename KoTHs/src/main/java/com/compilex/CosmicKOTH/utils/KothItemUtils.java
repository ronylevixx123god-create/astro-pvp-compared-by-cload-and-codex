/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.ArmorSet
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.HeroicWeaponType
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.armorsets.KothArmorSet
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.arkhamnetwork.Arkkit.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.compilex.CosmicKOTH.utils;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.ArmorSet;
import com.compilex.CosmicArmorSets.enums.HeroicWeaponType;
import com.compilex.CosmicArmorSets.enums.armorsets.KothArmorSet;
import com.compilex.CosmicKOTH.CosmicKOTHAPI;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KothItemUtils {
    public static SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yy");
    private static boolean armorSetsEnabled = false;
    private static boolean heroicWeapon = false;

    public static ItemStack getRandomKothItem(Player kothWinner) {
        return KothItemUtils.getRandomKothItem(kothWinner.getName());
    }

    public static ItemStack getRandomKothItem(String kothWinner) {
        int i = new Random().nextInt(8);
        if (i == 0) {
            return KothItemUtils.getKothHelmet(kothWinner);
        }
        if (i == 1) {
            return KothItemUtils.getKothChestplate(kothWinner);
        }
        if (i == 2) {
            return KothItemUtils.getKothLeggings(kothWinner);
        }
        if (i == 3) {
            return KothItemUtils.getKothBoots(kothWinner);
        }
        if (i == 4) {
            return KothItemUtils.getKothAxe(kothWinner);
        }
        if (i == 5) {
            return KothItemUtils.getKothSwordPvP(kothWinner);
        }
        if (i == 6) {
            return KothItemUtils.getKothSwordPvE(kothWinner);
        }
        if (i == 7) {
            return KothItemUtils.getKothBow(kothWinner);
        }
        return null;
    }

    public static ItemStack getKothHelmet(Player kothWinner) {
        return KothItemUtils.getKothHelmet(kothWinner.getName());
    }

    public static ItemStack getKothHelmetLegacy(String name) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_HELMET, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nHelmet&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + name + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Drunk", "Deathbringer", "Enlighted", "Armored", "Tank", "Angelic", "Implants");
        for (String s : customEnchantments) {
            KothItemUtils.addMaxedEnchant(EnchantmentAPI.getEnchantment((String)s), kothItem, true);
        }
        return EnchantmentBook.applyTransmogScroll((ItemStack)kothItem);
    }

    public static ItemStack getKothHelmet(String name) {
        if (armorSetsEnabled) {
            return ((KothArmorSet)ArmorSet.KOTH.getArmorSetInfo()).getHelmet(true, name);
        }
        return KothItemUtils.getKothHelmetLegacy(name);
    }

    public static ItemStack getKothChestplate(Player kothWinner) {
        return KothItemUtils.getKothChestplate(kothWinner.getName());
    }

    public static ItemStack getKothChestplate(String kothWinner) {
        if (armorSetsEnabled) {
            return ((KothArmorSet)ArmorSet.KOTH.getArmorSetInfo()).getChestplate(true, kothWinner);
        }
        return KothItemUtils.getKothChestplateLegacy(kothWinner);
    }

    protected static ItemStack getKothChestplateLegacy(String kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_CHESTPLATE, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nChestplate&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Blood Lust", "Overload", "Deathbringer", "Enlighted", "Armored", "Angelic", "Tank", "Dodge");
        for (String s : customEnchantments) {
            KothItemUtils.addMaxedEnchant(EnchantmentAPI.getEnchantment((String)s), kothItem, true);
        }
        return EnchantmentBook.applyTransmogScroll((ItemStack)kothItem);
    }

    public static ItemStack getKothLeggings(Player kothWinner) {
        return KothItemUtils.getKothLeggings(kothWinner.getName());
    }

    public static ItemStack getKothLeggings(String playerName) {
        if (armorSetsEnabled) {
            return ((KothArmorSet)ArmorSet.KOTH.getArmorSetInfo()).getLeggings(true, playerName);
        }
        return KothItemUtils.getKothLeggingsLegacy(playerName);
    }

    public static ItemStack getKothLeggingsLegacy(String playerName) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_LEGGINGS, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nLeggings&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + playerName + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Clarity", "Deathbringer", "Enlighted", "Armored", "Tank", "Angelic", "Valor", "Cactus");
        for (String s : customEnchantments) {
            KothItemUtils.addMaxedEnchant(EnchantmentAPI.getEnchantment((String)s), kothItem, true);
        }
        if (Math.random() < 0.075) {
            KothItemUtils.addMaxedEnchant(EnchantmentAPI.getEnchantment((String)"Nature Wrath"), kothItem, true);
        }
        return EnchantmentBook.applyTransmogScroll((ItemStack)kothItem);
    }

    public static ItemStack getKothBoots(Player kothWinner) {
        return KothItemUtils.getKothBoots(kothWinner.getName());
    }

    public static ItemStack getKothBoots(String winner) {
        if (armorSetsEnabled) {
            return ((KothArmorSet)ArmorSet.KOTH.getArmorSetInfo()).getBoots(true, winner);
        }
        return KothItemUtils.getKothBootsLegacy(winner);
    }

    public static ItemStack getKothBootsLegacy(String winner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_BOOTS, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nBoots&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + winner + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Deathbringer", "Enlighted", "Gears", "Armored", "Tank", "Angelic", "Rocket Escape");
        for (String s : customEnchantments) {
            KothItemUtils.addMaxedEnchant(EnchantmentAPI.getEnchantment((String)s), kothItem, true);
        }
        return EnchantmentBook.applyTransmogScroll((ItemStack)kothItem);
    }

    public static ItemStack getKothAxe(Player kothWinner) {
        return KothItemUtils.getKothAxe(kothWinner.getName());
    }

    public static ItemStack getKothAxe(String winner) {
        ItemStack retr = KothItemUtils.getKothAxeLegacy(winner);
        if (!heroicWeapon) {
            return EnchantmentBook.applyTransmogScroll((ItemStack)retr);
        }
        retr = CosmicArmorSetsAPI.convertIntoHeroicWeapon((HeroicWeaponType)HeroicWeaponType.AXE, (ItemStack)retr, (boolean)true);
        return CosmicKOTHAPI.applyKOTHWeaponSetBonus(retr, winner, HeroicWeaponType.AXE, "Deal +50% Durability Damage.");
    }

    public static ItemStack getKothAxeLegacy(String winner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_AXE, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nAxe&r"), (String[])new String[0]);
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Silence", "Lifesteal");
        for (String s : customEnchantments) {
            EnchantmentAPI.getEnchantment((String)s).addToItem(kothItem, EnchantmentAPI.getEnchantment((String)s).getMaxLevel());
        }
        if (Math.random() <= 0.25) {
            KothItemUtils.addRandomLeveledEnchant(EnchantmentAPI.getEnchantment((String)"Demonic Lifesteal"), kothItem);
        }
        return kothItem;
    }

    public static ItemStack getKothSwordPvP(Player kothWinner) {
        return KothItemUtils.getKothSwordPvP(kothWinner.getName());
    }

    public static ItemStack getKothSwordPvP(String winner) {
        ItemStack retr = KothItemUtils.getKothSwordPvPLegacy(winner);
        if (!heroicWeapon) {
            return EnchantmentBook.applyTransmogScroll((ItemStack)retr);
        }
        retr = CosmicArmorSetsAPI.convertIntoHeroicWeapon((HeroicWeaponType)HeroicWeaponType.SWORD, (ItemStack)retr, (boolean)true);
        return CosmicKOTHAPI.applyKOTHWeaponSetBonus(retr, winner, HeroicWeaponType.SWORD, "Deal +7.5% damage to all enemies.");
    }

    public static ItemStack getKothSwordPvPLegacy(String kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_SWORD, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nPvP Sword&r"), (String[])new String[0]);
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Lifesteal", "Silence");
        for (String s : customEnchantments) {
            kothItem = EnchantmentAPI.getEnchantment((String)s).addToItem(kothItem, EnchantmentAPI.getEnchantment((String)s).getMaxLevel());
        }
        if (Math.random() <= 0.25) {
            kothItem = KothItemUtils.addRandomLeveledEnchant(EnchantmentAPI.getEnchantment((String)"Demonic Lifesteal"), kothItem);
        }
        return kothItem;
    }

    public static ItemStack getKothSwordPvE(Player kothWinner) {
        return KothItemUtils.getKothSwordPvE(kothWinner.getName());
    }

    public static ItemStack getKothSwordPvE(String winner) {
        ItemStack sword = KothItemUtils.getKothSwordPvELegacy(winner);
        if (!heroicWeapon) {
            return EnchantmentBook.applyTransmogScroll((ItemStack)sword);
        }
        sword = EnchantmentAPI.removeEnchantments((ItemStack)sword);
        sword = CosmicArmorSetsAPI.convertIntoHeroicWeapon((HeroicWeaponType)HeroicWeaponType.SWORD, (ItemStack)sword, (boolean)true);
        return sword;
    }

    public static ItemStack getKothSwordPvELegacy(String kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.DIAMOND_SWORD, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nGrind Sword&r"), (String[])new String[0]);
        kothItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        kothItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 5);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Rage", "Inquisitive", "Kill Aura", "Double Strike", "Reforged", "Training");
        for (String s : customEnchantments) {
            EnchantmentAPI.getEnchantment((String)s).addToItem(kothItem, EnchantmentAPI.getEnchantment((String)s).getMaxLevel());
        }
        return kothItem;
    }

    public static ItemStack getKothBow(Player kothWinner) {
        return KothItemUtils.getKothBow(kothWinner.getName());
    }

    public static ItemStack getKothBow(String kothWinner) {
        ItemStack kothItem = ItemUtils.buildItem((Material)Material.BOW, (short)0, (String)ChatColor.translateAlternateColorCodes((char)'&', (String)"&f&l&k!&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k!&r &f&l&nBow&r"), (String[])new String[]{"", ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.AQUA + "Claimed by " + ChatColor.LIGHT_PURPLE.toString() + (Object)ChatColor.BOLD + kothWinner + (Object)ChatColor.AQUA + " on " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.UNDERLINE.toString() + format.format(new Date())});
        kothItem.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
        kothItem.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        kothItem.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        List<String> customEnchantments = Arrays.asList("Sniper", "Rage", "Hellfire", "Snare", "Hellfire", "Silence", "Piercing");
        for (String s : customEnchantments) {
            EnchantmentAPI.getEnchantment((String)s).addToItem(kothItem, EnchantmentAPI.getEnchantment((String)s).getMaxLevel());
        }
        if (Math.random() < 0.075) {
            EnchantmentAPI.getEnchantment((String)"Teleblock").addToItem(kothItem, EnchantmentAPI.getEnchantment((String)"Teleblock").getMaxLevel());
        }
        return EnchantmentBook.applyTransmogScroll((ItemStack)kothItem);
    }

    public static ItemStack addRandomLeveledEnchant(CustomEnchantment enchantment, ItemStack kothItem) {
        int level;
        int n = level = enchantment.getMaxLevel() == 1 ? 1 : ThreadLocalRandom.current().nextInt(enchantment.getMaxLevel()) + 1;
        if (enchantment.nonHeroicVersion != null) {
            enchantment.nonHeroicVersion.addToItem(kothItem, enchantment.nonHeroicVersion.getMaxLevel());
        }
        enchantment.addToItem(kothItem, level);
        return kothItem;
    }

    public static void addMaxedEnchant(CustomEnchantment enchantment, ItemStack kothItem, boolean makeHeroic) {
        enchantment.addToItem(kothItem, enchantment.getMaxLevel());
        if (makeHeroic && enchantment.heroicVersion != null && Math.random() <= 0.25) {
            enchantment.heroicVersion.addToItem(kothItem, enchantment.heroicVersion.getMaxLevel());
        }
    }

    public static boolean isArmorSetsEnabled() {
        return armorSetsEnabled;
    }

    public static boolean isHeroicWeapon() {
        return heroicWeapon;
    }

    static {
        armorSetsEnabled = new File("plugins/CosmicArmorSets.jar").exists();
        try {
            Class.forName("com.compilex.CosmicArmorSets.enums.HeroicWeaponType");
            heroicWeapon = true;
        }
        catch (ClassNotFoundException e) {
            Bukkit.getLogger().info("heroicWeapon = false due to no HeroicWeaponType!");
        }
    }
}

