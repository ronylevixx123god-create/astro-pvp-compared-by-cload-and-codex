/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI
 *  com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.google.common.collect.Lists
 *  com.massivecraft.factions.FPlayer
 *  com.rit.sucy.EnchantmentAPI
 *  me.ifamasssxd.cosmicduel.tasks.DuelArenaTask
 *  org.apache.commons.lang.CharUtils
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityChangeBlockEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerGameModeChangeEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerPortalEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.event.world.StructureGrowEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicduel.tasks.DuelArenaTask;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.patches.chat_filter.ChatUtils;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

final class PhazeListener
        implements Listener {
    private final List<Material> hollowMaterials;
    List<Material> disabledMaterials = Lists.newArrayList(Material.PAPER, Material.EXP_BOTTLE, Material.REDSTONE_TORCH_ON, Material.NAME_TAG, Material.CHEST, Material.CHEST, Material.TNT, Material.MONSTER_EGG, Material.MOB_SPAWNER);

    PhazeListener() {
        this.hollowMaterials = new ArrayList<Material>();
        for (String hollowMaterials : Arkkit.get().getConfig().getStringList("settings.anti_hack.hollow-materials")) {
            this.hollowMaterials.add(Material.getMaterial(hollowMaterials));
        }
    }

    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent e) {
        int y = e.getLocation().getBlockY();
        if (y > 200) {
            e.setCancelled(true);
            if (e.getPlayer() != null) {
                e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot grow objects past y:200");
            }
        }
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent e) {
        Player p;
        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && e.getEntity() instanceof Player && (p = (Player) e.getEntity()).getLocation().getBlock().getType() == Material.AIR && p.getEyeLocation().getBlock().getType() != Material.AIR && this.isNearEnchantmentTable(p)) {
            Bukkit.getLogger().info("[Arkkit (AntiHack)] Kicking " + p.getName() + " due to impossible block collision at " + p.getLocation() + "!");
            p.setMetadata("noCombatLog", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
            p.kickPlayer("Impossible block collision!");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClickHeadExploit(InventoryClickEvent e) {
        if (!Bukkit.getPluginManager().isPluginEnabled("Enchantments")) {
            return;
        }
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.SKULL_ITEM) {
            String possibleEnchantName;
            String weaponName;
            ItemStack i = e.getCurrentItem();
            String cosmicType = new NBTWrapper(i).getString("cosmicType");
            if (cosmicType.equalsIgnoreCase("COSMIC_MASK") || cosmicType.equalsIgnoreCase("inventory_pet")) {
                return;
            }
            if (i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().size() >= 4 && (weaponName = ChatColor.stripColor(i.getItemMeta().getLore().get(3))).contains(" ") && !weaponName.endsWith("!") && EnchantmentAPI.isRegistered(possibleEnchantName = weaponName.split(" ")[0])) {
                e.getWhoClicked().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Invalid lore found on player head, removed.");
                e.setCurrentItem(null);
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        String playerName = e.getPlayer().getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ncp removeplayer " + playerName + " MOVING_SURVIVALFLY");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.BEDROCK && !e.getPlayer().isOp()) {
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot place bedrock.");
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerShootBow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p;
            if (e.getForce() < 0.75f && !(p = (Player) e.getEntity()).hasMetadata("bowWarning")) {
                p.sendMessage("");
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "*** WEAK SHOT ***");
                p.sendMessage(ChatColor.GRAY + "Custom Bow Enchantments will not proc on targets unless your bow shot is charged at LEAST 75%");
                p.sendMessage("");
                p.setMetadata("bowWarning", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
            }
            e.getProjectile().setMetadata("shootForce", new FixedMetadataValue(Arkkit.get(), Float.valueOf(e.getForce())));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        if ((e.getMessage().equals("/hat") || e.getMessage().equals("/ehat") || e.getMessage().equals("/head") || e.getMessage().equals("/ehead") || e.getMessage().equals("/essentials:")) && e.getPlayer().getInventory().getHelmet() != null && e.getPlayer().getInventory().getHelmet().getType() != Material.AIR) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You cannot use " + e.getMessage() + " while you have a helmet equipped.");
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent e) {
        if (e.getTo() != null && e.getTo().getWorld() != null && e.getTo().getWorld().getName().startsWith("dungeon_") || e.getFrom() != null && e.getFrom().getWorld() != null && e.getFrom().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        if (FactionUtils.isWarZone(e.getFrom())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + " You cannot teleport to the nether from the Warzone.");
            if (e.getFrom().getBlock().getType() == Material.PORTAL && !e.getFrom().getBlock().hasMetadata("dungeonPortalBlock") && !e.getFrom().getBlock().hasMetadata("dungeonExitPortalBlock")) {
                e.getFrom().getBlock().setType(Material.AIR);
            }
            if (e.getFrom().clone().add(0.0, 1.0, 0.0).getBlock().getType() == Material.PORTAL && !e.getFrom().clone().add(0.0, 1.0, 0.0).getBlock().hasMetadata("dungeonPortalBlock") && !e.getFrom().clone().add(0.0, 1.0, 0.0).getBlock().hasMetadata("dungeonExitPortalBlock")) {
                e.getFrom().clone().add(0.0, 1.0, 0.0).getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerGamemodeChange(PlayerGameModeChangeEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        if (e.getNewGameMode() != GameMode.SURVIVAL) {
            e.setCancelled(true);
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPreCommandProcess(PlayerCommandPreprocessEvent e) {
        if ((e.getMessage().toLowerCase().startsWith("/f owner") || e.getMessage().toLowerCase().startsWith("/f unclaim") || e.getMessage().toLowerCase().startsWith("/f declaim") || e.getMessage().toLowerCase().startsWith("/f ownerunclaim") || e.getMessage().toLowerCase().startsWith("/f ownerun") || e.getMessage().toLowerCase().startsWith("/f claim")) && e.getMessage().split(" ").length == 3) {
            try {
                int radius = Integer.parseInt(e.getMessage().split(" ")[2]);
                if (radius > 20) {
                    e.getPlayer().sendMessage(ChatColor.RED + "You cannot process more than 20 chunks at a time.");
                    e.setCancelled(true);
                    e.setMessage("/null");
                    return;
                }
            } catch (Exception err) {
                if (err instanceof NumberFormatException) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Specify a numeric radius, 'all' is not valid for this command.");
                }
                err.printStackTrace();
                e.setCancelled(true);
                e.setMessage("/null");
            }
        }
        if (e.getMessage().toLowerCase().startsWith("/f title") && e.getMessage().toLowerCase().split(" ").length == 4) {
            String title = e.getMessage().toLowerCase().split(" ")[3];
            for (char c : title.toCharArray()) {
                if (CharUtils.isAsciiAlphanumeric(c) || c == '&') continue;
                e.setCancelled(true);
                e.setMessage("/null");
                e.getPlayer().sendMessage(ChatColor.RED + "You have invalid characters in your /f title");
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getRawSlot() == 2 && e.getCurrentItem() != null) {
                Player p = (Player) e.getWhoClicked();
                ItemStack is = e.getCurrentItem();
                if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                    ItemMeta im = is.getItemMeta();
                    if (this.containsIP(im.getDisplayName())) {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot have an IP address in your item name.");
                    }
                    if (this.containsURL(im.getDisplayName())) {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You cannot have a URL in your item name.");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (!e.getTo().getWorld().equals(e.getFrom().getWorld())) {
            e.getPlayer().setMetadata("arkkitTeleportEvent", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
        }
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onChestFall(EntityChangeBlockEvent e) {
        if (e.getTo() == Material.CHEST && DuelArenaTask.arena_regions.contains(WorldGuardUtils.getRegionName(e.getBlock().getLocation())) && (e.getBlock().getWorld().getName().equals("world_duels") || e.getBlock().getWorld().getName().equals("world_duels2"))) {
            this.removeDisabledItems(e.getBlock());
        }
    }

    @EventHandler
    public void onAnvilInventoryClick(InventoryClickEvent e) {
        Material monsterEgg = Material.getMaterial(383);
        if (e.getInventory().getType() == InventoryType.ANVIL && (e.getCurrentItem() != null && e.getCurrentItem().getType() == monsterEgg || e.getCursor() != null && e.getCursor().getType() == monsterEgg)) {
            final Player pl = (Player) e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            pl.updateInventory();
            Bukkit.getServer().getScheduler().runTaskLater(Arkkit.get(), new Runnable() {

                @Override
                public void run() {
                    pl.closeInventory();
                    pl.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " rename this item.");
                }
            }, 2L);
        }
    }

    public void removeDisabledItems(Block b) {
        if (b.getType() == Material.CHEST) {
            Chest chest = (Chest) b.getState();
            for (ItemStack is : chest.getInventory()) {
                if (is == null || is.getType() == Material.AIR || !this.disabledMaterials.contains(is.getType()))
                    continue;
                chest.getInventory().removeItem(is);
                ItemStack newItem = CosmicSpaceChestsAPI.getItems(ItemRarity.LEGENDARY, 1)[0];
                int trys = 100;
                while (this.disabledMaterials.contains(newItem.getType()) && trys > 0) {
                    if (--trys == 0) {
                        newItem = null;
                        break;
                    }
                    newItem = CosmicSpaceChestsAPI.getItems(ItemRarity.LEGENDARY, 1)[0];
                }
                if (newItem != null) {
                    chest.getInventory().addItem(newItem);
                    System.out.println("Adding back: " + newItem.toString());
                }
                chest.update();
                System.out.println("CosmicDuels: Removing disabled item from Envoy Duel Chest: " + (is.hasItemMeta() && is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : is.getType().name()));
            }
        }
    }

    public boolean containsIP(String name) {
        char[] chars = name.toCharArray();
        for (int index = 0; index < chars.length; ++index) {
            if (chars[index] == '.' || StringUtils.isAlphanumericSpace(String.valueOf(chars[index]))) continue;
            chars[index] = '\u0000';
        }
        name = new String(chars).replace(" ", "").replace("dot", ".");
        chars = null;
        return this.getAmount(name, '.') >= 2 || this.getNumbers(name) >= 8;
    }

    public boolean containsURL(String name) {
        return ChatUtils.checkMessageForWebPattern(name);
    }

    public int getNumbers(String s) {
        int i = 0;
        for (char c : s.toCharArray()) {
            if (!StringUtils.isNumeric(String.valueOf(c))) continue;
            ++i;
        }
        return i;
    }

    public int getAmount(String s, char chars) {
        char[] sList = s.toCharArray();
        int size = 0;
        for (char ch : sList) {
            if (ch != chars) continue;
            ++size;
        }
        sList = null;
        return size;
    }

    private boolean isNearEnchantmentTable(Player p) {
        for (int x = -2; x < 2; ++x) {
            for (int z = -2; z < 2; ++z) {
                for (int y = -2; y < 2; ++y) {
                    if (p.getLocation().add(x, y, z).getBlock().getType() != Material.ENCHANTMENT_TABLE)
                        continue;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSolidBlock(Location l) {
        return l.getBlock().getType() != Material.AIR && !this.hollowMaterials.contains(l.getBlock().getType());
    }
}

