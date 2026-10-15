/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.decimatemc.decimatelootboxes.api.LootboxManager
 *  com.google.common.collect.Lists
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.compilex.CosmicDungeons;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DungeonsRewardsAPI {
    private static List<String> heroicEnchantments = new ArrayList<String>(Arrays.asList("Planetary Deathbringer", "Master Inquisitive", "Divine Enlighted", "Godly Overload"));

    public static ItemStack getSoulAnvil() {
        return ItemUtils.addGlow(ItemUtils.buildItem(Material.ANVIL, (short)0, (Object)ChatColor.DARK_PURPLE + "" + (Object)ChatColor.BOLD + "Soul Anvil", (Object)ChatColor.LIGHT_PURPLE + "A powerful /dungeon trinket that", (Object)ChatColor.LIGHT_PURPLE + "heals durability of all equipped,", (Object)ChatColor.LIGHT_PURPLE + "armor by 10% in exchange for souls.", "", (Object)ChatColor.DARK_PURPLE + "" + (Object)ChatColor.UNDERLINE + "Soul Cost:" + (Object)ChatColor.LIGHT_PURPLE + " 100/use", (Object)ChatColor.DARK_PURPLE + "" + (Object)ChatColor.UNDERLINE + "Cooldown:" + (Object)ChatColor.LIGHT_PURPLE + " 30s"));
    }

    public static ItemStack getRandomHeroicEnchantment() {
        CustomEnchantment ce = EnchantmentAPI.getEnchantment((String)heroicEnchantments.get(new Random().nextInt(heroicEnchantments.size())));
        return EnchantmentBook.getEnchantmentBook((CustomEnchantment)ce, (int)(1 + new Random().nextInt(ce.getMaxLevel())));
    }

    public static ItemStack getDungeonLootBag(Player p, Dungeon d) {
        ArrayList lore = Lists.newArrayList((Object[])new String[]{(Object)ChatColor.GRAY + "A bag of dungeon loot obtained", (Object)ChatColor.GRAY + "by " + (Object)ChatColor.GOLD + p.getName() + (Object)ChatColor.GRAY + " from defeating the", d.getDungeonName() + (Object)ChatColor.GRAY + " dungeon!", ""});
        lore.add((Object)ChatColor.GRAY + "Contains " + (Object)ChatColor.GOLD + "??? Dungeon" + (Object)ChatColor.GRAY + " items...");
        if (d.isAbandondedSpaceship()) {
            return CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("AbandonedShipLootbag").createClaimableItem(false);
        }
        if (d.isDestroyedOutpost()) {
            return CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("DestroyedOutpostLootbag").createClaimableItem(false);
        }
        if (d.isPlanetNul()) {
            return CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("PlanetNulLootbag").createClaimableItem(false);
        }
        return null;
    }

    public static ItemStack getHeroicDungeonLootbag(Player p, Dungeon d) {
        ArrayList lore = Lists.newArrayList((Object[])new String[]{(Object)ChatColor.GRAY + "A bag of dungeon loot obtained", (Object)ChatColor.GRAY + "by " + (Object)ChatColor.GOLD + p.getName() + (Object)ChatColor.GRAY + " from defeating the", d.getDungeonName() + (Object)ChatColor.GRAY + " dungeon!", ""});
        lore.add((Object)ChatColor.GRAY + "Contains " + (Object)ChatColor.GOLD + "??? Dungeon" + (Object)ChatColor.GRAY + " items...");
        if (d.isAbandondedSpaceship()) {
            return CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("HeroicAbandonedShipLootbag").createClaimableItem(false);
        }
        if (d.isDestroyedOutpost()) {
            return CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("HeroicDestroyedOutpostLootbag").createClaimableItem(false);
        }
        if (d.isPlanetNul()) {
            return CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("HeroicPlanetNulLootbag").createClaimableItem(false);
        }
        return null;
    }

    public static boolean isDungeonLootBag(ItemStack i) {
        return i != null && i.getType() == Material.CHEST && i.hasItemMeta() && i.getItemMeta().hasLore() && ((String)i.getItemMeta().getLore().get(0)).equals((Object)ChatColor.GRAY + "A bag of dungeon loot obtained");
    }
}

