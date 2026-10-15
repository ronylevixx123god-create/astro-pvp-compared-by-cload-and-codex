/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.ArmorSet
 *  com.compilex.CosmicDungeons.DungeonsAPI
 *  com.compilex.CosmicDungeons.DungeonsRewardsAPI
 *  com.cosmicpvp.CosmicSpaceChests.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  org.arkhamnetwork.Arkkit.patches.Halloween
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.struct.gkits;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.ArmorSet;
import com.google.common.collect.Lists;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKit;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HeroicGrimReaper
extends GlobalKit {
    @Override
    public List<ItemStack> getKitItems(Player player, GlobalKits kits, int kitLevel) {
        ArrayList<ItemStack> rewards = Lists.newArrayList();
        ItemStack helmet = ItemUtils.buildItem(Material.DIAMOND_HELMET, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Hood of the Grim Reaper", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "Woven out of nether and ender com.cloth,", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "echoes of the dead can be heard from within.");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Deathbringer");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Sticky");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Enlighted");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Drunk");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Death God");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Resilience");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Aquatic");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Implants");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Repair Guard");
        HeroicGrimReaper.addRandomEnchantLevel(helmet, "Clarity");
        if (Math.random() < 0.05) {
            HeroicGrimReaper.addRandomEnchantLevel(helmet, "Paradox");
        }
        rewards.add(helmet);
        ItemStack chestplate = ItemUtils.buildItem(Material.DIAMOND_CHESTPLATE, (short)0, ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Tattered Robes of the Reaper", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "Distressed and disturbed, these black", ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + "robes devour all light before them.");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Diminish");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Overload");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Ghost");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Trickster");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Undead Ruse");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Ender Shift");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Leadership");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Obsidianshield");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Blood Lust");
        HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Lucky");
        if (Math.random() < 0.05) {
            HeroicGrimReaper.addRandomEnchantLevel(chestplate, "Nature Wrath");
        }
        rewards.add(chestplate);
        for (ItemStack armor : rewards) {
            armor.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
            armor.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
            armor.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 2);
        }
        ItemStack sword = ItemUtils.buildItem(Material.DIAMOND_SWORD, (short)0, ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Grim Reaper Scythe", ChatColor.RED.toString() + ChatColor.ITALIC.toString() + "The fabled death-forged scythe of", ChatColor.RED.toString() + ChatColor.ITALIC.toString() + "the almighty Grim Reaper itself.");
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Rage");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Silence");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Lifesteal");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Reforged");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Featherweight");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Demonforged");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Trap");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Blind");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Execute");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Paralyze");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Deep Wounds");
        HeroicGrimReaper.addRandomEnchantLevel(sword, "Double Strike");
        if (Math.random() < 0.1) {
            HeroicGrimReaper.addEnchant(sword, "Assassin", HeroicGrimReaper.getMaxEnchantLevel("Assassin"));
            HeroicGrimReaper.addRandomEnchantLevel(sword, "Shadow Assassin");
        } else {
            HeroicGrimReaper.addRandomEnchantLevel(sword, "Assassin");
        }
        if (Math.random() < 0.04) {
            HeroicGrimReaper.addRandomEnchantLevel(sword, "Divine Immolation");
        }
        rewards.add(sword);
        if (Math.random() < 0.005) {
            rewards.add(CosmicArmorSetsAPI.getArmorSet((ArmorSet)ArmorSet.PHANTOM)[random.nextInt(4)]);
        }
        if (Math.random() < 0.02) {
            rewards.add(com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils.getTitleVoucher((String)"GrimReaper"));
        }
        if (Math.random() <= 0.001) {
        }
        if (Math.random() <= 0.001) {
        }
        if (Math.random() <= 0.001) {
        }
        return rewards;
    }
}

