/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.HeroicArmorType
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.HeroicWeaponType
 *  com.google.common.collect.Lists
 *  org.arkhamnetwork.Arkkit.patches.NewYears
 *  org.arkhamnetwork.enchantments.EnchantmentOrb
 *  org.arkhamnetwork.enchantments.libs.OrbType
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.struct.gkits;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.HeroicArmorType;
import com.compilex.CosmicArmorSets.enums.HeroicWeaponType;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKit;
import org.arkhamnetwork.enchantments.EnchantmentOrb;
import org.arkhamnetwork.enchantments.libs.OrbType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeroicLoki
extends GlobalKit {
    @Override
    public List<ItemStack> getKitItems(Player player, GlobalKits kits, int kitLevel) {
        ArrayList<ItemStack> rewards = Lists.newArrayList();
        ItemStack helmet = this.setItemNameAndLore(kitLevel == 3 && Math.random() < 0.33 ? CosmicArmorSetsAPI.createHeroicPeice((HeroicArmorType)HeroicArmorType.HELMET) : new ItemStack(Material.DIAMOND_HELMET), ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Crown of Shadows", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "A dark crown, embroidered with absolute", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "black onyx. Icy cold to the touch.");
        HeroicLoki.addRandomEnchantLevel(helmet, "Ghost");
        if (Math.random() < (double)kitLevel * 0.25) {
            HeroicLoki.addEnchant(helmet, "Overload", HeroicLoki.getMaxEnchantLevel("Overload"));
            HeroicLoki.addRandomEnchantLevel(helmet, "Godly Overload");
        } else {
            HeroicLoki.addRandomEnchantLevel(helmet, "Overload");
        }
        if (Math.random() < (double)kitLevel * 0.25) {
            HeroicLoki.addEnchant(helmet, "Deathbringer", HeroicLoki.getMaxEnchantLevel("Deathbringer"));
            HeroicLoki.addRandomEnchantLevel(helmet, "Planetary Deathbringer");
        } else {
            HeroicLoki.addRandomEnchantLevel(helmet, "Deathbringer");
        }
        HeroicLoki.addRandomEnchantLevel(helmet, "Drunk");
        rewards.add(helmet);
        for (ItemStack armor : rewards) {
            armor.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
            armor.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            armor.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
        }
        ItemStack axe = this.setItemNameAndLore(new ItemStack(Material.DIAMOND_AXE), ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "Yixary", ChatColor.DARK_PURPLE.toString() + ChatColor.ITALIC.toString() + "A metaphysical shadow axe, considered", ChatColor.DARK_PURPLE.toString() + ChatColor.ITALIC.toString() + "by many to be a myth. It exists briefly", ChatColor.DARK_PURPLE.toString() + ChatColor.ITALIC.toString() + "only in the moments it kills.");
        axe.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        axe.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        HeroicLoki.addRandomEnchantLevel(axe, "Silence");
        if (Math.random() < 0.25) {
            HeroicLoki.addRandomEnchantLevel(axe, "Soul Trap");
        }
        if (Math.random() < (double)kitLevel * 0.33) {
            HeroicLoki.addEnchant(axe, "Cleave", HeroicLoki.getMaxEnchantLevel("Cleave"));
            HeroicLoki.addRandomEnchantLevel(axe, "Mighty Cleave");
        } else {
            HeroicLoki.addRandomEnchantLevel(axe, "Cleave");
        }
        if (Math.random() < (double)kitLevel * 0.33) {
            HeroicLoki.addEnchant(axe, "Lifesteal", HeroicLoki.getMaxEnchantLevel("Lifesteal"));
            HeroicLoki.addRandomEnchantLevel(axe, "Demonic Lifesteal");
        } else {
            HeroicLoki.addRandomEnchantLevel(axe, "Lifesteal");
        }
        HeroicLoki.addRandomEnchantLevel(axe, "Blessed");
        HeroicLoki.addRandomEnchantLevel(axe, "Insanity");
        HeroicLoki.addEnchant(axe, "Bleed", HeroicLoki.getMaxEnchantLevel("Bleed"));
        HeroicLoki.addRandomEnchantLevel(axe, "Deep Bleed");
        HeroicLoki.addRandomEnchantLevel(axe, "Hex");
        HeroicLoki.addRandomEnchantLevel(axe, "Corrupt");
        if (Math.random() < (double)kitLevel * 0.33) {
            axe = CosmicArmorSetsAPI.convertIntoHeroicWeapon((HeroicWeaponType)HeroicWeaponType.AXE, (ItemStack)axe, (boolean)true);
        }
        rewards.add(axe);
        if (Math.random() < 0.02 * (double)Math.max(1, kitLevel)) {
            ItemStack orb = null;
            if (kitLevel == 1) {
                orb = EnchantmentOrb.getEnchantmentOrb((int)100, (int)11, (OrbType)(new Random().nextBoolean() ? OrbType.ARMOR : OrbType.WEAPON));
            } else if (kitLevel == 2) {
                orb = Math.random() < 0.25 ? EnchantmentOrb.getEnchantmentOrb((int)(50 + new Random().nextInt(50)), (int)12, (OrbType)(new Random().nextBoolean() ? OrbType.ARMOR : OrbType.WEAPON)) : EnchantmentOrb.getEnchantmentOrb((int)100, (int)11, (OrbType)(new Random().nextBoolean() ? OrbType.ARMOR : OrbType.WEAPON));
            } else if (kitLevel == 3) {
                orb = Math.random() < 0.05 ? EnchantmentOrb.getEnchantmentOrb((int)(1 + new Random().nextInt(99)), (int)13, (OrbType)(new Random().nextBoolean() ? OrbType.ARMOR : OrbType.WEAPON)) : (Math.random() < 0.25 ? EnchantmentOrb.getEnchantmentOrb((int)(50 + new Random().nextInt(50)), (int)12, (OrbType)(new Random().nextBoolean() ? OrbType.ARMOR : OrbType.WEAPON)) : EnchantmentOrb.getEnchantmentOrb((int)100, (int)11, (OrbType)(new Random().nextBoolean() ? OrbType.ARMOR : OrbType.WEAPON)));
            }
            if (orb != null) {
                rewards.add(orb);
            }
        }
        return rewards;
    }
}

