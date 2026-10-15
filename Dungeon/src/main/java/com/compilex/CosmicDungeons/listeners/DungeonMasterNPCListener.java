/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.DecimalFormatType
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NumberUtils
 *  com.google.common.collect.Lists
 *  net.citizensnpcs.api.event.NPCRightClickEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package com.compilex.CosmicDungeons.listeners;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.menus.DungeonMasterKeyMenu;
import com.compilex.CosmicDungeons.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.struct.DecimalFormatType;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DungeonMasterNPCListener
implements Listener {
    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent e) {
        if (e.getNPC().getName().equals("Dungeon Master")) {
            e.getClicker().playSound(e.getClicker().getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 0.5f);
            new DungeonMasterKeyMenu(e.getClicker()).openGUI(CosmicDungeonsPlugin.get());
        }
    }

    public void onDungeonMasterInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getName().equals("Dungeon Master")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            Dungeon d = this.getDungeonByIndex(e.getRawSlot());
            if (d == null) {
                return;
            }
            Player p = (Player)e.getWhoClicked();
            if (CosmicDungeonsPlugin.get().getDisabledDungeons().contains((Object)d.getDungeonType())) {
                p.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot unlock that Dungeon Key at this time!");
                return;
            }
            if (d.playerHasPermission(p)) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You already have that dungeon key unlocked!");
                p.sendMessage((Object)ChatColor.GRAY + "Use " + (Object)ChatColor.UNDERLINE + "/dungeon" + (Object)ChatColor.GRAY + " to view your key management.");
                p.closeInventory();
                return;
            }
            if (!CosmicDungeonsPlugin.get_().getEconomy().has((OfflinePlayer)p, d.getDungeonType().getPortalCost())) {
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You do not have enough $ to unlock that dungeon key!");
                p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "COST " + (Object)ChatColor.WHITE + "$" + NumberUtils.format((double)d.getDungeonType().getPortalCost(), (DecimalFormatType)DecimalFormatType.MONEY));
                return;
            }
            CosmicDungeonsPlugin.get_().getEconomy().withdrawPlayer((OfflinePlayer)p, d.getDungeonType().getPortalCost());
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("dungeon givekey " + p.getName() + " " + d.getWorldName()));
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 0.8f, 0.4f);
            p.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + " - $" + NumberUtils.format((double)d.getDungeonType().getPortalCost(), (DecimalFormatType)DecimalFormatType.MONEY));
            p.sendMessage((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*** DUNGEON KEY UNLOCKED! ***");
            p.sendMessage((Object)ChatColor.GOLD + "You have unlocked the " + d.getDungeonName() + (Object)ChatColor.GOLD + " /dungeon key!");
            p.sendMessage((Object)ChatColor.GRAY + "You may now enter dungeon portals to that dungeon.");
        }
    }

    public Inventory getDungeonMasterMenu() {
        Inventory i = Bukkit.createInventory(null, (int)9, (String)"Dungeon Master");
        for (Dungeon d : DungeonsAPI.get().getDungeons()) {
            ArrayList lore = Lists.newArrayList((Object[])new String[]{"", (Object)ChatColor.GRAY + "Purchase this key to unlock access", (Object)ChatColor.GRAY + "to the associated dungeon.", "", (Object)ChatColor.GRAY + "Please note that you will still", (Object)ChatColor.GRAY + "need to craft/find a " + (Object)ChatColor.UNDERLINE + "dungeon portal", (Object)ChatColor.GRAY + "each time you wish to run this dungeon.", "", (Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "COST" + (Object)ChatColor.WHITE + " $" + NumberUtils.formatMoney((double)d.getDungeonType().getPortalCost())});
            if (CosmicDungeonsPlugin.get().getDisabledDungeons().contains((Object)d.getDungeonType())) {
                lore.add("");
                lore.add(CC.RedB + "COMING SOON");
            }
            i.setItem(d.getDungeonID(), new ItemBuilder(Material.TRIPWIRE_HOOK).name(d.getDungeonName() + " Key").lore((List)lore).build());
        }
        for (int x = 0; x < i.getSize(); ++x) {
            ItemStack is = i.getItem(x);
            if (is != null && is.getType() != Material.AIR) continue;
            i.setItem(x, ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short)7, (Object)ChatColor.BLACK + " "));
        }
        return i;
    }

    private Dungeon getDungeonByIndex(int x) {
        return DungeonsAPI.get().getDungeon(x);
    }
}

