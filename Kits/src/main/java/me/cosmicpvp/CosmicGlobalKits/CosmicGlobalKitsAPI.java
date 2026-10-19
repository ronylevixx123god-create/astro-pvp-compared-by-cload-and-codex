/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.CosmicDungeons.DungeonsRewardsAPI
 *  com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  com.cosmicpvp.CosmicSpaceChests.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.GeneralUtil
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.JSONMessage
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.arkhamnetwork.Arkkit.patches.Christmas
 *  org.arkhamnetwork.Arkkit.patches.Drinks
 *  org.arkhamnetwork.Arkkit.patches.Halloween
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.patches.NewYears
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.arkhamnetwork.enchantments.EnchantmentBook
 *  org.arkhamnetwork.enchantments.EnchantmentOrb
 *  org.arkhamnetwork.enchantments.RandomizationScroll
 *  org.arkhamnetwork.enchantments.libs.OrbType
 *  org.arkhamnetwork.enchantments.util.TieredEnchantments
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package me.cosmicpvp.CosmicGlobalKits;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKit;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitData;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitUser;
import me.cosmicpvp.CosmicGlobalKits.utils.GlobalCooldowns;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.arkhamnetwork.Arkkit.patches.*;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.EnchantmentOrb;
import org.arkhamnetwork.enchantments.RandomizationScroll;
import org.arkhamnetwork.enchantments.libs.OrbType;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CosmicGlobalKitsAPI {






    public static int getKitTier(Player player, GlobalKits kit) {
        GlobalKitData found;
        GlobalKitUser user = CosmicGlobalKits.getPlugin().getGlobalKitUser(player.getUniqueId());
        if (user != null && user.getGlobalKits() != null && (found = user.getGlobalKits().get((Object)kit)) != null) {
            return found.getKitTier();
        }
        return 0;
    }

    public static boolean canLevelUpGkit(Player player, GlobalKits kit) {
        GlobalKitUser user = CosmicGlobalKits.getPlugin().getGlobalKitUser(player.getUniqueId());
        if (user == null || user.getGlobalKits() == null || user.isLoadingData()) {
            return false;
        }
        GlobalKitData da = user.getGlobalKits().get((Object)kit);
        return da == null || da.getKitTier() < 3;
    }

    public static void registerGkitLevels(Player player, GlobalKits kit) {
        String playerName = player.getName();
        GlobalKitUser user = CosmicGlobalKits.getPlugin().getGlobalKitUser(player.getUniqueId());
        if (user == null) {
            Bukkit.getLogger().info("Unable to find GKit user for " + playerName);
            return;
        }
        Map<GlobalKits, GlobalKitData> data = user.getGlobalKits();
        if (!data.containsKey((Object)kit)) {
            data.put(kit, new GlobalKitData(0L, 0));
            Bukkit.getLogger().info("Registering global kit data for new kit claim for " + playerName + "!");
        }
    }



    public static List<ItemStack> getGlobalKitItems(GlobalKits gkit, String playerName) {
        return CosmicGlobalKitsAPI.getGlobalKitItems(gkit, playerName, false);
    }

    /*
     * WARNING - void declaration
     */
    public static List<ItemStack> getGlobalKitItems(GlobalKits gkit, String playerName, boolean preview) {
        List<ItemStack> rewards = new ArrayList<ItemStack>();
        try {

            if (gkit.getKitName().equals("engineer")) {


            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }

        for (ItemStack reward : rewards) {
            if (!GeneralUtil.isWeapon((ItemStack)reward) && !GeneralUtil.isArmor((ItemStack)reward)) continue;
            EnchantmentBook.applyTransmogScroll((ItemStack)reward);
        }
        return rewards;
    }
}

