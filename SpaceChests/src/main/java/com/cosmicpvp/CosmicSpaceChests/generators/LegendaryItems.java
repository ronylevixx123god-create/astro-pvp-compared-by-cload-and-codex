/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package com.cosmicpvp.CosmicSpaceChests.generators;

import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.rit.sucy.EnchantmentAPI;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public enum LegendaryItems {
    DRACULA_BLADE("dracula_blade", ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Dracula's Vampiric Blade", ChatColor.GOLD.toString() + ChatColor.ITALIC + "Sucks your victims dry of life energy.")),
    CTHULU_SWORD("cthulu_sword", ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Cthulu's Ancient Sword", ChatColor.GOLD.toString() + ChatColor.ITALIC + "An old sword with a destructive history.")),
    HERMES_BOOTS("hermes_boots", ItemUtils.buildItem(Material.DIAMOND_BOOTS, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Hereme's Winged Boots", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A pair of boots with wings attached to them.")),
    ARES_CHESTPLATE("ares_chestplate", ItemUtils.buildItem(Material.DIAMOND_CHESTPLATE, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Ares' War Chestplate", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A dense, heavy chestplate from the god of war.")),
    APOLLO_BOW("apollo_bow", ItemUtils.buildItem(Material.BOW, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Apollo's Bow", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A hand-crafted, gold embroidered bow of light.")),
    MJOLNIR("thor_axe", ItemUtils.buildItem(Material.DIAMOND_AXE, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Mjolnir", ChatColor.GOLD.toString() + ChatColor.ITALIC + "The fabled divine hammer of the Norse god, Thor.")),
    HAMMER_OF_HEPHAESTUS("hephaestus_axe", ItemUtils.buildItem(Material.DIAMOND_AXE, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Hammer of Hephaestus", ChatColor.GOLD.toString() + ChatColor.ITALIC + "The hammer of the Greek smith-god Hephaestus.")),
    LONGBOW_OF_THOR("thor_longbow", ItemUtils.buildItem(Material.BOW, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Longbow of Thor", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A thundering bow of explosive power.")),
    PANS_BOW_OF_COWIFICATION("pans_bow", ItemUtils.buildItem(Material.BOW, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Pans Bow of Cowification", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A mystical bow of ... Cows?!")),
    ARES_PLATEMAIL("ares_platemail", ItemUtils.buildItem(Material.DIAMOND_CHESTPLATE, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Platemail of Aegis the Protector", ChatColor.GOLD.toString() + ChatColor.ITALIC + "Protect your allies until your last breath.")),
    COSMIC_MILITARY_BLADE("military_blade", ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Cosmic Military Energy Blade", ChatColor.GOLD.toString() + ChatColor.ITALIC + "Electrifyingly dangerous.")),
    COSMIC_PARTY_HAT("party_hat", ItemUtils.buildItem(Material.DIAMOND_HELMET, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Cosmic Year #1 Party Hat", ChatColor.GOLD.toString() + ChatColor.ITALIC + "An extremely rare party hat, only", ChatColor.GOLD.toString() + ChatColor.ITALIC + "obtainable on the #1 anniversary weekend.", ChatColor.GOLD.toString() + ChatColor.ITALIC + "Now, party like it is 1999!")),
    SEPTRE_OF_DIO("party_septre", ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "The Septre of Dionysus", ChatColor.GOLD.toString() + ChatColor.ITALIC + "The legendary, fabled gem tipped", ChatColor.GOLD.toString() + ChatColor.ITALIC + "septre of Dionysus, god of partying.")),
    SHADOW_KILLER("shadow_killer", ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Shadow Killer", ChatColor.GOLD.toString() + ChatColor.ITALIC + "An onyx dagger with a dark legacy,", ChatColor.GOLD.toString() + ChatColor.ITALIC + "rumor has it this blade only", ChatColor.GOLD.toString() + ChatColor.ITALIC + "exists for a few fleeting moments", ChatColor.GOLD.toString() + ChatColor.ITALIC + "before and after an assassination.")),
    VOID_SANDALS("void_sandals", ItemUtils.buildItem(Material.DIAMOND_BOOTS, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Erebus' Sandals of the Void", ChatColor.GOLD.toString() + ChatColor.ITALIC + "You walk on the thin line between", ChatColor.GOLD.toString() + ChatColor.ITALIC + "existence and extinction, do your", ChatColor.GOLD.toString() + ChatColor.ITALIC + "enemies dare to follow?")),
    CHAOS_EXECUTIONER("chaos_executioner", ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Chaos Executioner", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A daemon forged greatsword,", ChatColor.GOLD.toString() + ChatColor.ITALIC + "wieldable only to those the blade", ChatColor.GOLD.toString() + ChatColor.ITALIC + "itself deems worthy, and to kill", ChatColor.GOLD.toString() + ChatColor.ITALIC + "those it deems unworthy.")),
    HEPH_PICKAXE("heph_pickaxe", ItemUtils.buildItem(Material.DIAMOND_PICKAXE, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Pickaxe of Hephaestus", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A legendary tool forged by", ChatColor.GOLD.toString() + ChatColor.ITALIC + "the cosmic god of builders.")),
    SHIVA_SPADE("shiva_spade", ItemUtils.buildItem(Material.DIAMOND_SPADE, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Shiva's Excavator", ChatColor.GOLD.toString() + ChatColor.ITALIC + "An ancient spade use by", ChatColor.GOLD.toString() + ChatColor.ITALIC + "the cosmic god of excavation.")),
    FREEDOM_HAT("freedom_hat", ItemUtils.buildItem(Material.DIAMOND_HELMET, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Freedom Hat", ChatColor.GOLD.toString() + ChatColor.ITALIC + "Free from the chains of the ancient", ChatColor.GOLD.toString() + ChatColor.ITALIC + "masters, you are the captain of your", ChatColor.GOLD.toString() + ChatColor.ITALIC + "own destiny.")),
    RELIC_OF_JUSTICE("relic_of_justice", ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Relic of Astraea", ChatColor.GOLD.toString() + ChatColor.ITALIC + "A mystical runeblade imbued with,", ChatColor.GOLD.toString() + ChatColor.ITALIC + "the virtue of Astraea, godess of justice."));

    private final ItemStack is;

    LegendaryItems(String sys_name, ItemStack base_stack) {
        if (sys_name.equals("relic_of_justice")) {
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
            base_stack.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
            EnchantmentAPI.getEnchantment("Rage").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Rage").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Lifesteal").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Lifesteal").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Featherweight").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Featherweight").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Blind").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Blind").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Execute").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Execute").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Silence").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Silence").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Trap").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Trap").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Inquisitive").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Inquisitive").getMaxLevel()) + 1);
        }
        if (sys_name.equals("freedom_hat")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, 3);
            base_stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            EnchantmentAPI.getEnchantment("Leadership").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Leadership").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Lucky").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Lucky").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Smoke Bomb").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Smoke Bomb").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Overload").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Overload").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Enlighted").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Enlighted").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Commander").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Commander").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Angelic").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Angelic").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Glowing").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Glowing").getMaxLevel()) + 1);
        }
        if (sys_name.equals("shiva_spade")) {
            base_stack.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
            base_stack.addEnchantment(Enchantment.DIG_SPEED, 5);
            base_stack.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, new Random().nextInt(3) + 1);
            EnchantmentAPI.getEnchantment("Detonate").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Detonate").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Telepathy").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Telepathy").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Oxygenate").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Oxygenate").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Haste").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Haste").getMaxLevel()) + 1);
        }
        if (sys_name.equals("heph_pickaxe")) {
            base_stack.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
            base_stack.addEnchantment(Enchantment.DIG_SPEED, 4 + new Random().nextInt(2));
            base_stack.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, new Random().nextInt(3) + 1);
            EnchantmentAPI.getEnchantment("Auto Smelt").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Auto Smelt").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Detonate").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Detonate").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Experience").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Experience").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Haste").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Haste").getMaxLevel()) + 1);
        }
        if (sys_name.equals("chaos_executioner")) {
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
            base_stack.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
            EnchantmentAPI.getEnchantment("Demonforged").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Demonforged").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Disarmor").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Disarmor").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Execute").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Execute").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Headless").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Headless").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Trap").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Trap").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Silence").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Silence").getMaxLevel()) + 1);
        }
        if (sys_name.equals("void_sandals")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, 3);
            base_stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            EnchantmentAPI.getEnchantment("Ender Shift").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Ender Shift").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Ender Walker").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Ender Walker").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Stormcaller").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Stormcaller").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Deathbringer").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Deathbringer").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Anti Gravity").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Anti Gravity").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Voodoo").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Voodoo").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Gears").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Gears").getMaxLevel()) + 1);
        }
        if (sys_name.equals("shadow_killer")) {
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
            base_stack.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
            EnchantmentAPI.getEnchantment("Silence").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Silence").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Skill Swipe").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Skill Swipe").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Block").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Block").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Double Strike").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Double Strike").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Featherweight").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Featherweight").getMaxLevel()) + 1);
        }
        if (sys_name.equals("party_septre")) {
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 4);
            base_stack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
            base_stack.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
            EnchantmentAPI.getEnchantment("Insomnia").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Insomnia").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Epicness").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Epicness").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Trap").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Trap").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Lifesteal").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Lifesteal").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Inquisitive").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Inquisitive").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Featherweight").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Featherweight").getMaxLevel()) + 1);
        }
        if (sys_name.equals("party_hat")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, 3);
            base_stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            EnchantmentAPI.getEnchantment("Drunk").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Drunk").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Aquatic").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Aquatic").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Glowing").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Glowing").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Implants").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Implants").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Enlighted").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Enlighted").getMaxLevel()) + 1);
        }
        if (sys_name.equals("military_blade")) {
            base_stack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
            base_stack.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
            EnchantmentAPI.getEnchantment("Obliterate").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Obliterate").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Inquisitive").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Inquisitive").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Paralyze").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Paralyze").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Double Strike").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Double Strike").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Featherweight").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Featherweight").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Headless").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Headless").getMaxLevel()) + 1);
        }
        if (sys_name.equals("ares_platemail")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, 3);
            base_stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            EnchantmentAPI.getEnchantment("Spirit Link").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Spirit Link").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Self Destruct").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Self Destruct").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Armored").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Armored").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Overload").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Overload").getMaxLevel()) + 1);
            EnchantmentAPI.getEnchantment("Enlighted").addToItem(base_stack, new Random().nextInt(EnchantmentAPI.getEnchantment("Enlighted").getMaxLevel()) + 1);
        }
        if (sys_name.equals("dracula_blade")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, new Random().nextInt(3) + 1);
            base_stack.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addEnchantment(Enchantment.FIRE_ASPECT, 2);
            EnchantmentAPI.getEnchantment("Blind").addToItem(base_stack, new Random().nextInt(3) + 1);
            EnchantmentAPI.getEnchantment("Lifesteal").addToItem(base_stack, 5);
            EnchantmentAPI.getEnchantment("Vampire").addToItem(base_stack, new Random().nextInt(3) + 1);
        }
        if (sys_name.equals("cthulu_sword")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, new Random().nextInt(3) + 1);
            base_stack.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addEnchantment(Enchantment.FIRE_ASPECT, 2);
            base_stack.addEnchantment(Enchantment.KNOCKBACK, new Random().nextInt(2) + 1);
            EnchantmentAPI.getEnchantment("Thundering Blow").addToItem(base_stack, new Random().nextInt(3) + 1);
            EnchantmentAPI.getEnchantment("Deep Wounds").addToItem(base_stack, new Random().nextInt(3) + 1);
        }
        if (sys_name.equals("hermes_boots")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, new Random().nextInt(3) + 1);
            base_stack.addEnchantment(Enchantment.PROTECTION_FALL, new Random().nextInt(2) + 3);
            base_stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            base_stack.addEnchantment(Enchantment.THORNS, new Random().nextInt(2) + 2);
            EnchantmentAPI.getEnchantment("Gears").addToItem(base_stack, new Random().nextInt(2) + 2);
            EnchantmentAPI.getEnchantment("Springs").addToItem(base_stack, new Random().nextInt(2) + 2);
        }
        if (sys_name.equals("ares_chestplate")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, new Random().nextInt(2) + 2);
            base_stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            base_stack.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, new Random().nextInt(2) + 3);
            base_stack.addEnchantment(Enchantment.PROTECTION_FIRE, new Random().nextInt(2) + 3);
            base_stack.addEnchantment(Enchantment.PROTECTION_PROJECTILE, new Random().nextInt(2) + 3);
            base_stack.addEnchantment(Enchantment.THORNS, 3);
            EnchantmentAPI.getEnchantment("Implants").addToItem(base_stack, new Random().nextInt(2) + 2);
            EnchantmentAPI.getEnchantment("Hardened").addToItem(base_stack, new Random().nextInt(2) + 2);
            EnchantmentAPI.getEnchantment("Overload").addToItem(base_stack, new Random().nextInt(3) + 1);
        }
        if (sys_name.equals("apollo_bow")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, new Random().nextInt(3) + 1);
            base_stack.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
            base_stack.addEnchantment(Enchantment.ARROW_FIRE, 1);
            base_stack.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            base_stack.addEnchantment(Enchantment.ARROW_KNOCKBACK, new Random().nextInt(2) + 1);
            EnchantmentAPI.getEnchantment("Thundering Blow").addToItem(base_stack, 3);
            EnchantmentAPI.getEnchantment("Piercing").addToItem(base_stack, 3);
        }
        if (sys_name.equals("thor_axe")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, new Random().nextInt(3) + 1);
            base_stack.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
            base_stack.addUnsafeEnchantment(Enchantment.KNOCKBACK, new Random().nextInt(2) + 1);
            EnchantmentAPI.getEnchantment("Berserk").addToItem(base_stack, 4);
            EnchantmentAPI.getEnchantment("Blessed").addToItem(base_stack, 3);
            EnchantmentAPI.getEnchantment("Bleed").addToItem(base_stack, 5);
        }
        if (sys_name.equals("hephaestus_axe")) {
            base_stack.addEnchantment(Enchantment.DURABILITY, new Random().nextInt(3) + 1);
            base_stack.addEnchantment(Enchantment.DAMAGE_ALL, 5);
            base_stack.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
            base_stack.addUnsafeEnchantment(Enchantment.KNOCKBACK, new Random().nextInt(2) + 1);
            EnchantmentAPI.getEnchantment("Decapitation").addToItem(base_stack, 3);
            EnchantmentAPI.getEnchantment("Blessed").addToItem(base_stack, 3);
            EnchantmentAPI.getEnchantment("Reforged").addToItem(base_stack, 8);
            EnchantmentAPI.getEnchantment("Ravenous").addToItem(base_stack, 4);
        }
        this.is = base_stack.clone();
    }

    public ItemStack getItem() {
        return this.is.clone();
    }

    public static boolean isLegendaryItem(ItemStack is) {
        return is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(ChatColor.GOLD.toString());
    }
}

