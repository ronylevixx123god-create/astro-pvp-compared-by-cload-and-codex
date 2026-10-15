/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.ArmorSet
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.HeroicArmorType
 *  com.google.common.collect.Lists
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.struct.gkits;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.ArmorSet;
import com.compilex.CosmicArmorSets.enums.HeroicArmorType;
import com.google.common.collect.Lists;
import com.rit.sucy.EnchantmentAPI;
import java.util.ArrayList;
import java.util.List;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKit;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeroicArenaChampion
extends GlobalKit {
    @Override
    public List<ItemStack> getKitItems(Player player, GlobalKits kits, int kitLevel) {
        boolean phantomSycthe;
        ArrayList<ItemStack> rewards = Lists.newArrayList();
        ItemStack helmet = ItemUtils.buildItem(Material.DIAMOND_HELMET, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Crown of the Colosseum Champion", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "A golden crown embedded with bloodstones,", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "beware the monstrosity whom dawns this sigil.");
        helmet = CosmicArmorSetsAPI.convertItemToHeroic((ItemStack)helmet, (HeroicArmorType)HeroicArmorType.HELMET, (boolean)true);
        HeroicArenaChampion.addRandomEnchantLevel(helmet, "Guardians");
        if (Math.random() <= 0.1 + 0.1 * (double)kitLevel) {
            HeroicArenaChampion.addEnchant(helmet, "Overload", HeroicArenaChampion.getMaxEnchantLevel("Overload"));
            HeroicArenaChampion.addRandomEnchantLevel(helmet, "Godly Overload");
        } else {
            HeroicArenaChampion.addRandomEnchantLevel(helmet, "Overload");
        }
        if (Math.random() <= 0.1 + 0.1 * (double)kitLevel) {
            HeroicArenaChampion.addEnchant(helmet, "Deathbringer", HeroicArenaChampion.getMaxEnchantLevel("Deathbringer"));
            HeroicArenaChampion.addRandomEnchantLevel(helmet, "Planetary Deathbringer");
        } else {
            HeroicArenaChampion.addRandomEnchantLevel(helmet, "Deathbringer");
        }
        if (Math.random() <= 0.1 + 0.1 * (double)kitLevel) {
            HeroicArenaChampion.addEnchant(helmet, "Armored", HeroicArenaChampion.getMaxEnchantLevel("Armored"));
            HeroicArenaChampion.addRandomEnchantLevel(helmet, "Paladin Armored");
        } else {
            HeroicArenaChampion.addRandomEnchantLevel(helmet, "Armored");
        }
        HeroicArenaChampion.addRandomEnchantLevel(helmet, "Implants");
        HeroicArenaChampion.addRandomEnchantLevel(helmet, "Tank");
        HeroicArenaChampion.addRandomEnchantLevel(helmet, "Valor");
        rewards.add(helmet);
        for (ItemStack armor : rewards) {
            armor.addEnchantment(Enchantment.DURABILITY, 3);
            armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            armor.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        }
        boolean bl = phantomSycthe = Math.random() <= 0.03 + 0.03 * (double)kitLevel;
        ItemStack sword = phantomSycthe ? CosmicArmorSetsAPI.getWeaponFromArmorSet((ArmorSet)ArmorSet.PHANTOM) : ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Bloodstone Rapier of Kronos", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "A blade with such a dark lineage", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "its very existence has been condemned", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "by the Council of Cosmonauts.");
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 2);
        if (phantomSycthe) {
            sword = EnchantmentAPI.removeEnchantments((ItemStack)sword);
        } else {
            if (Math.random() <= 0.25 + 0.25 * (double)kitLevel) {
                HeroicArenaChampion.addEnchant(sword, "Lifesteal", HeroicArenaChampion.getMaxEnchantLevel("Lifesteal"));
                HeroicArenaChampion.addRandomEnchantLevel(sword, "Demonic Lifesteal");
            } else {
                HeroicArenaChampion.addRandomEnchantLevel(sword, "Lifesteal");
            }
            if (Math.random() <= 0.1 + 0.1 * (double)kitLevel) {
                HeroicArenaChampion.addEnchant(sword, "Assassin", HeroicArenaChampion.getMaxEnchantLevel("Assassin"));
                HeroicArenaChampion.addRandomEnchantLevel(sword, "Shadow Assassin");
            } else {
                HeroicArenaChampion.addRandomEnchantLevel(sword, "Assassin");
            }
            HeroicArenaChampion.addRandomEnchantLevel(sword, "Rage");
            HeroicArenaChampion.addRandomEnchantLevel(sword, "Silence");
            HeroicArenaChampion.addRandomEnchantLevel(sword, "Paralyze");
            HeroicArenaChampion.addRandomEnchantLevel(sword, "Trap");
            HeroicArenaChampion.addRandomEnchantLevel(sword, "Double Strike");
            HeroicArenaChampion.addRandomEnchantLevel(sword, "Vampire");
            if (Math.random() <= 0.15 + 0.15 * (double)kitLevel) {
                HeroicArenaChampion.addRandomEnchantLevel(sword, "Divine Immolation");
            }
        }
        rewards.add(sword);
        return rewards;
    }
}

