/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.factions.points.FactionPointUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TimeUtils
 *  com.google.common.collect.Lists
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.compilex.CosmicDungeons.menus;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.compilex.CosmicDungeons.items.DungeonKeyItem;
import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DungeonMasterKeyMenu
extends CosmicGUI {
    public DungeonMasterKeyMenu(Player player) {
        super(player, "Dungeon Master", 9);
    }

    public static boolean canBuyKey(DungeonType type) {
        return DungeonMasterKeyMenu.getResetString(type) == null;
    }

    public static String getResetString(DungeonType type) {
        boolean notAvailable;
        long timeSinceStart = 10;
        long timeIsUnlocked = timeSinceStart + type.getDungeonStartOffset();
        boolean bl = notAvailable = timeIsUnlocked > System.currentTimeMillis() && timeSinceStart > 0L;
        if (notAvailable) {
            return TimeUtils.formatFutureTime((long)timeIsUnlocked);
        }
        return null;
    }

    public void redraw() {
        int index = 0;
        for (DungeonType dungeonType : DungeonType.values()) {
            if (CosmicDungeonsPlugin.get().getDisabledDungeons().contains((Object)dungeonType)) continue;
            DungeonKeyItem item = new DungeonKeyItem(dungeonType);
            ItemBuilder builder = new ItemBuilder(item.build());
            String resetString = DungeonMasterKeyMenu.getResetString(dungeonType);
            ArrayList toAdd = Lists.newArrayList((Object[])new String[]{"", (resetString != null ? CC.RedB + CC.Strike : CC.RedB) + "COST " + (resetString != null ? CC.White + CC.Strike : CC.White) + "$" + NumberUtils.formatMoney((double)dungeonType.getPortalCost())});
            if (resetString != null) {
                toAdd.add(CC.Red + "Available in " + CC.RedB + resetString + CC.Red + "...");
            }
            builder.addLoreLines((List)toAdd);
            this.setItem(index++, new InventoryItem(builder.build()).click(() -> {
                if (!CosmicDungeonsPlugin.get_().getEconomy().has((OfflinePlayer)this.player, dungeonType.getPortalCost())) {
                    this.player.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You do not have enough $ to unlock that dungeon portal key!");
                    this.player.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "COST " + (Object)ChatColor.WHITE + "$" + NumberUtils.formatMoney((double)dungeonType.getPortalCost()));
                    return;
                }
                if (resetString != null) {
                    this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must wait " + CC.RedU + DungeonMasterKeyMenu.getResetString(dungeonType) + CC.Red + " before this /dungeon is available!");
                    return;
                }
                if (this.player.getInventory().firstEmpty() == -1) {
                    this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Your inventory is currently full!");
                    return;
                }
                if (CosmicDungeonsPlugin.get_().getEconomy().withdrawPlayer((OfflinePlayer)this.player, dungeonType.getPortalCost()).transactionSuccess()) {
                    PlayerUtil.giveOrDropProtectedItem((Player)this.player, (ItemStack)item.build(), (int)-1);
                    this.player.playSound(this.player.getLocation(), Sound.ORB_PICKUP, 0.8f, 0.4f);
                    this.player.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + " - $" + NumberUtils.formatMoney((double)dungeonType.getPortalCost()));
                    this.player.sendMessage((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*** DUNGEON KEY PURCHASED! ***");
                    this.player.sendMessage((Object)ChatColor.GOLD + "You have purchased a " + dungeonType.getDungeonName() + (Object)ChatColor.GOLD + " /dungeon portal key!");
                    this.player.sendMessage((Object)ChatColor.GRAY + "You may now enter dungeon portals to that dungeon, which will consume this key!");
                }
            }));
        }
        for (int i = 0; i < this.size; ++i) {
            ItemStack found = this.inventory.getItem(i);
            if (found != null && found.getType() != Material.AIR) continue;
            this.setItem(i, new InventoryItem(new ItemBuilder(Material.STAINED_GLASS_PANE, "&7 ", new String[0]).data((short)DyeColor.GRAY.getDyeData()).build()));
        }
    }
}

