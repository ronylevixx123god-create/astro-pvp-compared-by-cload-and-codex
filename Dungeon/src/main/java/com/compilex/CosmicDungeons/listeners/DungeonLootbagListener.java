/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.enums.ArmorSet
 *  com.compilex.CosmicArmorSets.enums.HeroicArmorType
 *  com.compilex.CosmicArmorSets.enums.HeroicWeaponType
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  com.cosmicpvp.CosmicSpaceChests.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.itembuilder.AbilityArmorSetItemBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.CustomItemBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.CustomItemBuilder$CustomItemType
 *  com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder
 *  com.cosmicpvp.cosmicutils.itembuilder.KitGemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.factions.points.FactionPointUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.JSONMessage
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.UpgradeUtils
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  me.ifamasssxd.cosmicconquest.CosmicConquestAPI
 *  me.ifamasssxd.cosmicdisguises.managers.CosmicDisguiseAPI
 *  me.ifamasssxd.killtracker.KillTrackerAPI
 *  org.arkhamnetwork.Arkkit.libs.ParticleEffect
 *  org.arkhamnetwork.Arkkit.patches.EquipmentLootboxes
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.patches.ItemLinking
 *  org.arkhamnetwork.Arkkit.patches.LoreTag
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 *  org.arkhamnetwork.enchantments.EnchantmentOrb
 *  org.arkhamnetwork.enchantments.RandomizationScroll
 *  org.arkhamnetwork.enchantments.libs.OrbType
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 */
package com.compilex.CosmicDungeons.listeners;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.ArmorSet;
import com.compilex.CosmicArmorSets.enums.HeroicArmorType;
import com.compilex.CosmicArmorSets.enums.HeroicWeaponType;
import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.DungeonsRewardsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.itembuilder.AbilityArmorSetItemBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.CustomItemBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.itembuilder.KitGemBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmicconquest.CosmicConquestAPI;
import me.ifamasssxd.cosmicdisguises.managers.CosmicDisguiseAPI;
import me.ifamasssxd.cosmicguishop.ArkhamMoneyNotesAPI;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.patches.ItemLinking;
import org.arkhamnetwork.Arkkit.patches.LoreTag;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.EnchantmentOrb;
import org.arkhamnetwork.enchantments.RandomizationScroll;
import org.arkhamnetwork.enchantments.libs.OrbType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DungeonLootbagListener
implements Listener {
    /*
     * Enabled aggressive block sorting
     */

    public List<ItemStack> getAllDungeonLoot(DungeonType dungeonType) {
        List<ItemStack> item = this.getCoolDungeonLoot(dungeonType);
        item.addAll(this.getOtherDungeonLoot(dungeonType));
        return item;
    }

    public List<ItemStack> getCoolDungeonLoot(DungeonType dungeonType) {
        boolean isDestroyed = dungeonType.equals((Object)DungeonType.DESTROYED_OUTPOST);
        ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        loot.add(ArkhamMoneyNotesAPI.getMoneyNote("Dungeon", random.nextInt(50000000) + 15000000));
        loot.add(ExpBottles.getXpBottle(random.nextInt(75000) + 150000, "Dungeon"));
        loot.add(CrateKeyItem.getItem(CrateType.valueOf("SEASON")));
        return loot;
    }

    public List<ItemStack> addLoot(ItemBuilder builder, List<ItemStack> items) {
        ItemStack item = builder.build();
        if (item != null) {
            items.add(item);
        }
        return items;
    }

    public List<ItemStack> getOtherDungeonLoot(DungeonType dungeonType) {
        int mcmmoCredits;
        int moneyToGive;
        boolean isDestroyedOutpost = dungeonType.equals((Object)DungeonType.DESTROYED_OUTPOST);
        ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        loot.add(ArkhamMoneyNotesAPI.getMoneyNote("Dungeon", random.nextInt(50000000) + 15000000));
        loot.add(ExpBottles.getXpBottle(random.nextInt(75000) + 150000, "Dungeon"));
        loot.add(CrateKeyItem.getItem(CrateType.valueOf("SEASON")));
        return loot;
    }

    private int getEmptyInventorySlots(Player p) {
        int emptySlots = 0;
        for (ItemStack i : p.getInventory()) {
            if (i != null && i.getType() != Material.AIR) continue;
            ++emptySlots;
        }
        return emptySlots;
    }
}

