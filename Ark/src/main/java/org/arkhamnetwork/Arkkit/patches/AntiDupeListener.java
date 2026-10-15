/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.gmail.nossr50.datatypes.player.McMMOPlayer
 *  com.gmail.nossr50.datatypes.skills.AbilityType
 *  com.gmail.nossr50.util.ItemUtils
 *  com.gmail.nossr50.util.player.UserManager
 *  com.massivecraft.factions.Board
 *  com.massivecraft.factions.FLocation
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.FactionWrapper
 *  com.massivecraft.factions.FactionWrappers
 *  org.arkhamnetwork.enchantments.util.GeneralUtil
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPhysicsEvent
 *  org.bukkit.event.block.BlockPistonExtendEvent
 *  org.bukkit.event.block.BlockPistonRetractEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.enchantment.EnchantItemEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityPortalEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.CraftItemEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryMoveItemEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.massivecraft.factions.*;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

final class AntiDupeListener
        implements Listener {
    private static final HashSet<Material> oreMaterials = new HashSet<Material>(Arrays.asList(Material.COAL_ORE, Material.IRON_ORE, Material.DIAMOND_ORE, Material.REDSTONE_ORE, Material.EMERALD_ORE, Material.GOLD_ORE, Material.LAPIS_ORE, Material.QUARTZ_ORE));
    Plugin plugin = null;
    boolean volcanic_crates = false;

    public AntiDupeListener(ArkPlugin ark_plugin) {
        this.plugin = ark_plugin.getBukkitPlugin();
        this.volcanic_crates = new File("plugins/VolcanicCrates.jar").exists();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEnchantItemEvent(EnchantItemEvent e) {
        e.getEnchantsToAdd().remove(Enchantment.DEPTH_STRIDER);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace_LOWEST(BlockPlaceEvent event) {
        if (!this.volcanic_crates || event.getPlayer().isOp()) {
            return;
        }
        if (event.getBlock().getType() == Material.TRIPWIRE || event.getBlock().getType() == Material.TRIPWIRE_HOOK) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace_MONITOR(BlockPlaceEvent event) {
        if (!this.volcanic_crates || event.getPlayer().isOp()) {
            return;
        }
        if (event.getBlock().getType() == Material.TRIPWIRE || event.getBlock().getType() == Material.TRIPWIRE_HOOK) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.CREEPER && (e.getCause() == EntityDamageEvent.DamageCause.MAGIC || e.getCause() == EntityDamageEvent.DamageCause.WITHER || e.getCause() == EntityDamageEvent.DamageCause.POISON)) {
            e.setCancelled(true);
            e.setDamage(0.0);
        }
    }

    @EventHandler
    public void onEntityPortalEnter(EntityPortalEvent e) {
        if (e.getEntity() instanceof Player) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemMoveInventory(InventoryMoveItemEvent e) {
        if (e.getItem().getType() == Material.POTION && e.getItem().getAmount() > 1) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAnvilRenameDust(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getRawSlot() == 2 && e.getCurrentItem() != null) {
                Player p = (Player) e.getWhoClicked();
                ItemStack is = e.getCurrentItem();
                if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                    ItemMeta im = is.getItemMeta();
                    if (!this.canRenameItem(is)) {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot rename this item.");
                        p.sendMessage(ChatColor.GRAY + "Anvil renaming can only be used on armor, weapons, and tools.");
                    }
                }
            }
        }
    }

    private boolean canRenameItem(ItemStack i) {
        return i != null && (GeneralUtil.isArmor(i) || GeneralUtil.isTool(i) || GeneralUtil.isWeapon(i));
    }



    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Material changedType = event.getChangedType();
        Material blockType = event.getBlock().getType();
        if (!(blockType != Material.SUGAR_CANE && blockType != Material.SUGAR_CANE_BLOCK && blockType != Material.CACTUS && blockType != Material.BROWN_MUSHROOM && blockType != Material.RED_MUSHROOM && blockType != Material.WHEAT && blockType != Material.CROPS && blockType != Material.SAPLING && blockType != Material.DOUBLE_PLANT && blockType != Material.FLOWER_POT && blockType != Material.ANVIL && blockType != Material.CACTUS || changedType != Material.CHEST && changedType != Material.TRAPPED_CHEST && changedType != Material.WOODEN_DOOR && changedType != Material.WOOD_DOOR && changedType != Material.IRON_DOOR && changedType != Material.IRON_DOOR_BLOCK && changedType != Material.TRAP_DOOR && changedType != Material.REDSTONE_COMPARATOR && changedType != Material.REDSTONE_COMPARATOR_OFF && changedType != Material.DIODE_BLOCK_OFF && changedType != Material.DIODE_BLOCK_ON && changedType != Material.FLOWER_POT && changedType != Material.CACTUS)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFakeRedstoneWirePlaceEvent(BlockPhysicsEvent e) {
        if ((e.getChangedType().name().contains("REDSTONE") || e.getChangedType().name().contains("DIODE")) && e.getBlock().getType() == Material.FENCE_GATE) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onCraftItemEvent(CraftItemEvent e) {
        Player pl = (Player) e.getWhoClicked();
        if (pl.getInventory().firstEmpty() == -1) {
            e.setCancelled(true);
            pl.updateInventory();
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " craft without any free space!");
        }
        if (e.getRecipe().getResult().getType() == Material.BEACON) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            pl.updateInventory();
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " craft beacon blocks!");
        }
        if (e.getRecipe().getResult().getType() == Material.EYE_OF_ENDER) {
            return;
        }
        for (ItemStack i : e.getInventory()) {
            if (i == null || !i.hasItemMeta() || !i.getItemMeta().hasLore() || i.getItemMeta().getLore().size() <= 0 || i.getType() == Material.DIAMOND_PICKAXE)
                continue;
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            pl.updateInventory();
            pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " craft using materials that have custom lore.");
            return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportComplete(PlayerTeleportEvent e) {
        if (e.getFrom().getWorld().equals(e.getTo().getWorld()) || e.getFrom().getWorld().getName().equals("world_duels")) {
            if (e.getPlayer().hasMetadata("natureWrathTask") && e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
                return;
            }
            e.getPlayer().setMetadata("lastTeleportEvent", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.hasMetadata("lastTeleportEvent") && System.currentTimeMillis() - p.getMetadata("lastTeleportEvent").get(0).asLong() < 250L) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot perform inventory edits so quickly after teleporting.");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPlaceCreeperEgg(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.hasItem() && e.getItem().getType() == Material.MONSTER_EGG && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && p.hasMetadata("lastTeleportEvent") && System.currentTimeMillis() - p.getMetadata("lastTeleportEvent").get(0).asLong() < 5000L) {
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot use monster eggs so quickly after teleporting (5s).");
        }
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        ArrayList<ItemStack> toRemove = new ArrayList<ItemStack>();
        for (ItemStack is : e.getPlayer().getInventory()) {
            if (is == null || !is.hasItemMeta() || !is.getItemMeta().hasLore()) continue;
            for (String s : is.getItemMeta().getLore()) {
                if (!ChatColor.stripColor(s).equals("Click to toggle this setting.")) continue;
                toRemove.add(is);
            }
        }
        for (ItemStack is : toRemove) {
            Bukkit.getLogger().info("(AntiCostItems) Removing " + is.getType().name() + " from " + e.getPlayer().getName() + "!");
            e.getPlayer().getInventory().remove(is);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPistonPush(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            if (!oreMaterials.contains(b.getType())) continue;
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPistonPull(BlockPistonRetractEvent e) {
        BlockFace direction = e.getDirection();
        Block movedBlock = e.getBlock().getRelative(direction).getRelative(direction);
        if (oreMaterials.contains(movedBlock.getType())) {
            e.setCancelled(true);
        }
    }
}

