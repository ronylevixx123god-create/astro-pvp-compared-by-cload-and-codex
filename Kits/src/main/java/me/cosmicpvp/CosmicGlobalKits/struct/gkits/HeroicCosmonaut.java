/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.HeroicArmorType
 *  com.google.common.collect.Lists
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.bukkit.ChatColor
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.struct.gkits;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.HeroicArmorType;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeroicCosmonaut
extends GlobalKit {
    @Override
    public List<ItemStack> getKitItems(Player player, GlobalKits kits, int kitLevel) {
        ArrayList<ItemStack> rewards = Lists.newArrayList();
        ArrayList<CustomEnchantment> enchants = new ArrayList<CustomEnchantment>();
        ItemStack helmet = this.setItemNameAndLore(CosmicArmorSetsAPI.createHeroicPeice((HeroicArmorType)HeroicArmorType.CHESTPLATE), ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + (player != null ? player.getName() : "???") + "'s Cosmonaut Spacesuit", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "Forged of the finest materials in all", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "the galaxy. A timeless, unique piece.");
        ItemStack legs = this.setItemNameAndLore(CosmicArmorSetsAPI.createHeroicPeice((HeroicArmorType)HeroicArmorType.LEGGINGS), ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + (player != null ? player.getName() : "???") + "'s Cosmonaut Pantaloons", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "Created out of the richest materials", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "in all the galaxy. So fabulous.");
        enchants.add(EnchantmentAPI.getEnchantment((String)"Immortal"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Enlighted"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Obsidianshield"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Ghost"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Creeper Armor"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Guardians"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Leadership"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Overload"));
        enchants.add(EnchantmentAPI.getEnchantment((String)"Deathbringer"));
        Collections.shuffle(enchants);
        for (int i = 0; i < enchants.size(); ++i) {
            boolean heroicAdded;
            if (Math.random() < 0.1 && i != 0) continue;
            CustomEnchantment enchant = (CustomEnchantment)enchants.get(i);
            double chance = kitLevel == 1 ? 0.05 : (kitLevel == 2 ? 0.1 : (kitLevel == 3 ? 0.2 : 0.0));
            boolean bl = heroicAdded = Math.random() < chance && enchant.heroicVersion != null;
            if (random.nextBoolean()) {
                enchant.addToItem(helmet, heroicAdded ? enchant.getMaxLevel() : random.nextInt(enchant.getMaxLevel()) + 1);
                if (!heroicAdded) continue;
                enchant.heroicVersion.addToItem(helmet, enchant.heroicVersion.getMaxLevel() == 1 ? 1 : random.nextInt(enchant.heroicVersion.getMaxLevel()) + 1);
                continue;
            }
            enchant.addToItem(legs, heroicAdded ? enchant.getMaxLevel() : random.nextInt(enchant.getMaxLevel()) + 1);
            if (!heroicAdded) continue;
            enchant.heroicVersion.addToItem(legs, enchant.heroicVersion.getMaxLevel() == 1 ? 1 : random.nextInt(enchant.heroicVersion.getMaxLevel()) + 1);
        }
        rewards.add(helmet);
        rewards.add(legs);
        for (ItemStack armor : rewards) {
            armor.addEnchantment(Enchantment.DURABILITY, 3);
            armor.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        }
        return rewards;
    }
}

