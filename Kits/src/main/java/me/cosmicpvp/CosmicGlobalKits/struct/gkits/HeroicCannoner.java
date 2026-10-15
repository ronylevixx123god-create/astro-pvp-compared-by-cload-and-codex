/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.ArmorSet
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.WeaponedArmorSet
 *  com.compilex.CosmicCustomCreepers.me.astropvp.cratekeys.enums.CreeperTypes
 *  com.google.common.collect.Lists
 *  org.arkhamnetwork.CosmicCustomTNT.me.astropvp.cratekeys.enums.TNTTypes
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.struct.gkits;

import com.compilex.CosmicArmorSets.enums.ArmorSet;
import com.compilex.CosmicArmorSets.enums.WeaponedArmorSet;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKit;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeroicCannoner
extends GlobalKit {
    @Override
    public List<ItemStack> getKitItems(Player player, GlobalKits kits, int kitLevel) {
        ArrayList<ItemStack> rewards = Lists.newArrayList();
        ItemStack helmet = ItemUtils.buildItem(Material.DIAMOND_HELMET, (short) 0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Heroic Mining Helmet", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "An enhanced version of the famous,", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "mining helmet, your vision penetrates all.");
        HeroicCannoner.addEnchant(helmet, "Aquatic", HeroicCannoner.getMaxEnchantLevel("Aquatic"));
        HeroicCannoner.addEnchant(helmet, "Clarity", HeroicCannoner.getMaxEnchantLevel("Clarity"));
        HeroicCannoner.addEnchant(helmet, "Molten", HeroicCannoner.getMaxEnchantLevel("Molten"));
        HeroicCannoner.addEnchant(helmet, "Creeper Armor", HeroicCannoner.getMaxEnchantLevel("Creeper Armor"));
        HeroicCannoner.addEnchant(helmet, "ObsidianShield", HeroicCannoner.getMaxEnchantLevel("ObsidianShield"));
        rewards.add(helmet);
        ItemStack chest = ItemUtils.buildItem(Material.DIAMOND_CHESTPLATE, (short) 0, ChatColor.GOLD.toString() + "Heroic Onyx Cloak of the Grandmaster", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "A well kept, leather black cloak.");
        HeroicCannoner.addEnchant(chest, "Shockwave", HeroicCannoner.getMaxEnchantLevel("Shockwave"));
        HeroicCannoner.addEnchant(chest, "Ragdoll", HeroicCannoner.getMaxEnchantLevel("Ragdoll"));
        HeroicCannoner.addEnchant(chest, "Overload", HeroicCannoner.getMaxEnchantLevel("Overload"));
        if (0.1 + (double) kitLevel * 0.075 > Math.random()) {
            HeroicCannoner.addRandomEnchantLevel(chest, "Godly Overload");
        }
        rewards.add(chest);
        ItemStack leggings = ItemUtils.buildItem(Material.DIAMOND_LEGGINGS, (short) 0, ChatColor.GOLD.toString() + "Heroic Leather Combat Leggings", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "Absolute mobility, complete security");
        HeroicCannoner.addEnchant(leggings, "Lifebloom", HeroicCannoner.getMaxEnchantLevel("Lifebloom"));
        HeroicCannoner.addEnchant(leggings, "Nutrition", HeroicCannoner.getMaxEnchantLevel("Nutrition"));
        HeroicCannoner.addEnchant(leggings, "Enlighted", HeroicCannoner.getMaxEnchantLevel("Enlighted"));
        if (0.05 + (double) kitLevel * 0.075 > Math.random()) {
            HeroicCannoner.addRandomEnchantLevel(chest, "Divine Enlighted");
        }
        rewards.add(leggings);
        ItemStack boots = ItemUtils.buildItem(Material.DIAMOND_BOOTS, (short) 0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Heroic Ghost Sandals", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "A pair of sandals from the depths of the", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "void, wear these and become undetecable.");
        HeroicCannoner.addEnchant(boots, "Gears", HeroicCannoner.getMaxEnchantLevel("Gears"));
        HeroicCannoner.addEnchant(boots, "Springs", HeroicCannoner.getMaxEnchantLevel("Springs"));
        HeroicCannoner.addEnchant(boots, "Ender Walker", HeroicCannoner.getMaxEnchantLevel("Ender Walker"));
        HeroicCannoner.addEnchant(boots, "Lucky", HeroicCannoner.getMaxEnchantLevel("Lucky"));
        if (0.025 + (double) kitLevel * 0.025 > Math.random()) {
            int maxPossible = 2 + kitLevel;
            HeroicCannoner.addEnchant(boots, "Infinite Luck", random.nextInt(maxPossible) + 1);
        }
        HeroicCannoner.addEnchant(boots, "Ghost", HeroicCannoner.getMaxEnchantLevel("Ghost"));
        if (0.25 + (double) kitLevel * 0.1 > Math.random()) {
            HeroicCannoner.addRandomEnchantLevel(boots, "Ghostly Ghost");
        }
        HeroicCannoner.addEnchant(boots, "Rocket Escape", HeroicCannoner.getMaxEnchantLevel("Rocket Escape"));
        if (0.1 + (double) kitLevel * 0.1 > Math.random()) {
            HeroicCannoner.addRandomEnchantLevel(boots, "Guided Rocket Escape");
        }
        rewards.add(boots);
        for (ItemStack armor : rewards) {
            armor.addEnchantment(Enchantment.DURABILITY, 3);
            armor.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            armor.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        }
        ItemStack bow = ItemUtils.buildItem(Material.BOW, (short) 0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Heroic Crystal Bow of Athena", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "When you get your target within", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "your sights, their journey ends.");
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, Enchantment.ARROW_DAMAGE.getMaxLevel());
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, random.nextInt(Enchantment.ARROW_KNOCKBACK.getMaxLevel()) + 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, random.nextInt(Enchantment.ARROW_INFINITE.getMaxLevel()) + 1);
        bow.addEnchantment(Enchantment.DURABILITY, 3);
        HeroicCannoner.addEnchant(bow, "Sniper", HeroicCannoner.getMaxEnchantLevel("Sniper"));
        HeroicCannoner.addEnchant(bow, "Snare", HeroicCannoner.getMaxEnchantLevel("Snare"));
        HeroicCannoner.addEnchant(bow, "Lightning", HeroicCannoner.getMaxEnchantLevel("Lightning"));
        HeroicCannoner.addEnchant(bow, "Piercing", HeroicCannoner.getMaxEnchantLevel("Piercing"));
        HeroicCannoner.addEnchant(bow, "Explosive", HeroicCannoner.getMaxEnchantLevel("Explosive"));
        HeroicCannoner.addEnchant(bow, "Silence", HeroicCannoner.getMaxEnchantLevel("Silence"));
        HeroicCannoner.addEnchant(bow, "Teleportation", HeroicCannoner.getMaxEnchantLevel("Teleportation"));
        if (0.5 + (double) kitLevel * 0.15 > Math.random()) {
            HeroicCannoner.addRandomEnchantLevel(bow, "Bidirectional Teleportation");
        }
        rewards.add(bow);
        rewards.add(new ItemStack(Material.ARROW, 1));
        for (int x = kitLevel + 1; x > 0; --x) {
            int x1 = random.nextInt(3);
            int x2 = random.nextInt(3);

            int vanillaTntStacks = 1 + new Random().nextInt(3 + kitLevel);
            while (vanillaTntStacks-- > 0) {
                rewards.add(new ItemStack(Material.TNT, 64));
            }
            for (int i = 0; i < 4; ++i) {
                ItemStack customCreeper = null;

                if (customCreeper != null) {
                    customCreeper.setAmount(1 + kitLevel);
                }
                rewards.add(customCreeper);
            }
            rewards.add(new ItemStack(Material.MONSTER_EGG, 3 + new Random().nextInt(kitLevel * 2 + 1), (short) 50));
            rewards.add(new ItemStack(Material.SPONGE, 5 + kitLevel * 2));
            rewards.add(new ItemStack(Material.DISPENSER, 16 + kitLevel * 16));
            if (Math.random() < (double) kitLevel * 0.2) {
                rewards.add(((WeaponedArmorSet) ArmorSet.RANGER.getArmorSetInfo()).getWeapon(false));
            }
        }
        return rewards;

    }
}

