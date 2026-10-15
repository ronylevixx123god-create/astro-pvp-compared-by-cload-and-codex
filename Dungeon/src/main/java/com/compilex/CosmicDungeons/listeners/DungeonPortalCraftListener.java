/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.inventory.CraftItemEvent
 *  org.bukkit.inventory.CraftingInventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.Recipe
 *  org.bukkit.inventory.ShapedRecipe
 */
package com.compilex.CosmicDungeons.listeners;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.compilex.CosmicDungeons.items.DungeonKeyItem;
import com.compilex.CosmicDungeons.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class DungeonPortalCraftListener
implements Listener {
    public static final ItemStack enchantedObsidian = ItemUtils.buildItem(Material.OBSIDIAN, (short)0, ChatColor.AQUA.toString() + (Object)ChatColor.BOLD + "Enchanted Obsidian", (Object)ChatColor.GRAY + "Special obsidian used to construct", (Object)ChatColor.GRAY + "the infamous /dungeon portals.");
    public static final ItemStack dimensionWeb = ItemUtils.buildItem(Material.WEB, (short)0, ChatColor.GOLD.toString() + (Object)ChatColor.BOLD + "Dimension Web", (Object)ChatColor.GRAY + "A web of powerful arcane magic", (Object)ChatColor.GRAY + "that can be harnassed for instantaneous", (Object)ChatColor.GRAY + "interstellar /dungeon teleportation.");
    public static final ItemStack cosmonautFuel = ItemUtils.buildItem(Material.REDSTONE_TORCH_ON, (short)0, (Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "Cosmonaut Fuel Cell", (Object)ChatColor.GRAY + "Next-Gen Cosmonaut space tech,", (Object)ChatColor.GRAY + "used to stabilize the output of", (Object)ChatColor.GRAY + "Dimension Webs for /dungeons.");
    public static final ItemStack dimensionalCore = new NBTWrapper(new ItemBuilder(Material.BEACON, CC.LightPurpleB + "Interdimensional Core", new String[]{CC.Gray + "A strange crystal emanating a powerful", CC.Gray + "ominous aura. You sense you will need other", CC.Gray + "components for this object to be useful", CC.Gray + "to you in your /dungeon runs."}).build()).setString("cosmicType", "dimensionalCore").build();
    public static final ItemStack teleporterCrystal = new NBTWrapper(new ItemBuilder(Material.FIREWORK_CHARGE, CC.WhiteB + "Teleportation Crystal", new String[]{CC.Gray + "An indescent pulsating gem obtained", CC.Gray + "from the destroyed bridge of the", CC.Gray + "Abandoned Spaceship /dungeon. The", CC.Gray + "last remnants left by the cosmonauts", CC.Gray + "before they mysteriously vanished"}).build()).setString("cosmicType", "teleporterCrystal").build();

    public DungeonPortalCraftListener() {
        for (Dungeon d : DungeonsAPI.get().getDungeons()) {
            ItemStack dungeonPortal = ItemUtils.buildItem(Material.EYE_OF_ENDER, (short)0, (Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "Dungeon Portal: " + d.getDungeonName(), (Object)ChatColor.GRAY + "Right-click to create a", (Object)ChatColor.GOLD + "" + (Object)ChatColor.UNDERLINE + "Dungeon Portal" + (Object)ChatColor.GRAY + " to " + d.getDungeonName(), "", (Object)ChatColor.GOLD + "Use " + (Object)ChatColor.UNDERLINE + "/dungeon" + (Object)ChatColor.GOLD + " for more information.", "", (Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "WARNING: " + (Object)ChatColor.RED + "The portal will only be", (Object)ChatColor.RED + "stable for 100 seconds.");
            d.setDungeonPortal(dungeonPortal);
            ShapedRecipe portalRecipe = new ShapedRecipe(dungeonPortal);
            portalRecipe.shape(new String[]{"***", "*x*", "*i*"});
            if (d.getDungeonType() == DungeonType.DESTROYED_OUTPOST) {
                portalRecipe.setIngredient('*', enchantedObsidian.getData());
                portalRecipe.setIngredient('x', dimensionalCore.getData());
                portalRecipe.setIngredient('i', teleporterCrystal.getData());
            } else {
                portalRecipe.setIngredient('*', enchantedObsidian.getData());
                portalRecipe.setIngredient('x', dimensionWeb.getData());
                portalRecipe.setIngredient('i', cosmonautFuel.getData());
            }
            Bukkit.getServer().addRecipe((Recipe)portalRecipe);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onCraftItem(CraftItemEvent e) {
        if (e.getRecipe().getResult().getType() == Material.EYE_OF_ENDER) {
            boolean outpostCrafting;
            CraftingInventory craftingInventory = e.getInventory();
            boolean bl = outpostCrafting = DungeonsAPI.get().isDimensionalCore(craftingInventory.getItem(5)) && DungeonsAPI.get().isTransportationCrystal(craftingInventory.getItem(8));
            if (DungeonsAPI.get().isEnchantedObsidian(craftingInventory.getItem(1)) && DungeonsAPI.get().isEnchantedObsidian(craftingInventory.getItem(2)) && DungeonsAPI.get().isEnchantedObsidian(craftingInventory.getItem(3)) && DungeonsAPI.get().isEnchantedObsidian(craftingInventory.getItem(4)) && (outpostCrafting || DungeonsAPI.get().isDimensionWeb(craftingInventory.getItem(5))) && DungeonsAPI.get().isEnchantedObsidian(craftingInventory.getItem(6)) && DungeonsAPI.get().isEnchantedObsidian(craftingInventory.getItem(7)) && (outpostCrafting || DungeonsAPI.get().isCosmonautFuel(craftingInventory.getItem(8))) && DungeonsAPI.get().isEnchantedObsidian(craftingInventory.getItem(9))) {
                if (!e.isCancelled()) {
                    if (e.isShiftClick()) {
                        e.setResult(Event.Result.DENY);
                        e.setCancelled(true);
                        ((Player)e.getWhoClicked()).sendMessage(CC.RedB + "(!) " + CC.Red + "Please avoid shift click crafting multiple Dungeon Portals!");
                        return;
                    }
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("contests add " + e.getWhoClicked().getName() + " portalsCreated " + e.getCurrentItem().getAmount()));
                    Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent((Player)e.getWhoClicked(), "DUNGEON_PORTAL_CRAFT", new Object[]{e.getCurrentItem(), e.getInventory()}));
                }
                return;
            }
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
    public void onAttemptToPlaceIngredient(BlockPlaceEvent e) {
        if (DungeonsAPI.get().isEnchantedObsidian(e.getItemInHand()) || DungeonsAPI.get().isDimensionWeb(e.getItemInHand()) || DungeonsAPI.get().isCosmonautFuel(e.getItemInHand())) {
            e.getPlayer().sendMessage((Object)ChatColor.RED + "You cannot place this rare Dungeon Portal ingredient.");
            e.getPlayer().sendMessage((Object)ChatColor.GRAY + "Use /dungeon for more information.");
            e.setCancelled(true);
        } else if (DungeonKeyItem.getDungeonFromKey(e.getItemInHand()) != null) {
            e.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot place a Dungeon Portal Key!");
            e.getPlayer().sendMessage(CC.Gray + "Enter a /dungeon portal to consume this key!");
            e.setCancelled(true);
        }
    }
}

