/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 */
package com.compilex.CosmicKOTH.utils;

import com.compilex.CosmicKOTH.CosmicKOTHAPI;
import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.compilex.CosmicKOTH.struct.KothWorldType;
import com.compilex.CosmicKOTH.tasks.DateWatcherTask;
import com.compilex.CosmicKOTH.tasks.KothEventTask;
import com.compilex.CosmicKOTH.tasks.LauncherTask;
import com.compilex.CosmicKOTH.utils.RewardUtils;
import com.compilex.CosmicKOTH.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtils {
    public static Inventory getKothInventory() {
        Inventory inventory = Bukkit.createInventory(null, (int)9, (String)"King of the Hill");
        inventory.setItem(4, InventoryUtils.getKothInventoryItem());
        inventory.setItem(8, InventoryUtils.getGuide());
        int slot = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                inventory.setItem(slot, InventoryUtils.getFillerItem());
            }
            ++slot;
        }
        return inventory;
    }

    public static ItemStack getKothInventoryItem() {
        String status = CosmicKOTHAPI.canTeleportToKOTH() ? ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "OPEN" : ChatColor.RED.toString() + ChatColor.BOLD.toString() + "CLOSED";
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.GoldB + "KOTH Information " + CC.Gray + "(/koth)");
        ArrayList lore = Lists.newArrayList();
        lore.add("");
        lore.add(CC.WhiteB + "KOTH Status: " + status);
        lore.add(CC.WhiteB + "Location: " + CC.LightPurpleB + "Desert Koth " + CC.Gray + "(X2500, Y19, Z4600)");
        KothWorldType worldType = KothWorldType.getFromIndex(RewardUtils.getMapIndex());
        if (CosmicKOTHAPI.isKOTHRunning()) {
            KothEventTask KET = CosmicKOTHPlugin.get_().getKoth();
            if (worldType != null) {
                lore.add(CC.WhiteB + "Arena: " + CC.GoldB + worldType.getName(true));
            }
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Runtime: " + (Object)ChatColor.GREEN + TimeUtils.getSecondsAsString(KET.getRuntime()));
            String flagStatus = null;
            if (KET.getPlayerCapped() != null) {
                flagStatus = ChatColor.RED.toString() + ChatColor.BOLD.toString() + "CAPTURED " + (Object)ChatColor.GRAY + "(" + (Object)ChatColor.GREEN + KET.getNameOfFlagCapper() + (Object)ChatColor.GRAY + ")";
            }
            if (KET.getPlayerCapping() != null) {
                flagStatus = ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "CAPTURING " + (Object)ChatColor.GRAY + "(" + (Object)ChatColor.GREEN + KET.getNameOfFlagCapper() + (Object)ChatColor.GRAY + ") [" + (Object)ChatColor.GREEN + TimeUtils.getSecondsAsString(KET.getSecondsUntilCap()) + (Object)ChatColor.GRAY + " left]";
            }
            if (flagStatus == null) {
                flagStatus = ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "UNCLAIMED";
            }
            lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Flag: " + flagStatus);
            lore.add("");
            if (CosmicKOTHAPI.canTeleportToKOTH()) {
                lore.add(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.GRAY + "Use " + (Object)ChatColor.GREEN + (Object)ChatColor.UNDERLINE + "/warp koth" + (Object)ChatColor.GRAY + " to join the event!");
            } else {
                lore.add(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.GRAY + "This KOTH has been capped by " + (Object)ChatColor.GREEN + (Object)ChatColor.UNDERLINE + KET.getPlayerCapped().getName() + "!");
            }
        } else {
            if (LauncherTask.countdown != 0) {
                lore.add(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "Next Scheduled KOTH: " + ChatColor.RED.toString() + ChatColor.BOLD.toString() + TimeUtils.getSecondsAsString(LauncherTask.countdown));
            }
            }
        lore.add("");
        lore.add(CC.GoldB + "Winner Reward:");
        lore.add(CC.WhiteB + "● " + CC.Gray + "1x Koth Lootbag");
        lore.add(CC.WhiteB + "● " + CC.Gray + "10$ Buycraft Giftcard");
        lore.add("");
        lore.add(CC.translate("&6&lFaction Bonus:"));
        lore.add(CC.translate("&f&l● &7+10% Outgoing Damage"));
        lore.add(CC.translate("&f&l● &7+10% Crystal Apply Chance"));
        lore.add(CC.translate("&f&l● &7+5% Chance to keep Slot-Bot tickets on roll"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getFillerItem() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)DyeColor.BLACK.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate((String)"&f"));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getGuide() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.GoldB + CC.Bold + "King of the Hill");
        ArrayList lore = Lists.newArrayList();
        lore.add("");
        lore.add(CC.Gray + "KOTH is a " + CC.White + "popular factions minigame " + CC.Gray + "where");
        lore.add(CC.Gray + "players compete for control of a central");
        lore.add(CC.Gray + "capturable region held at the every " + CC.GrayU + "4 hours");
        lore.add("");
        lore.add(CC.Gray + "If you can " + CC.White + "keep control of the region" + CC.Gray + " for");
        lore.add(CC.Yellow + "5 minutes, " + CC.Gray + "you will be rewarded with some");
        lore.add(CC.Gray + "of the " + CC.GrayU + "best loot in the Cosmoverse!");
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        return item;
    }
}

