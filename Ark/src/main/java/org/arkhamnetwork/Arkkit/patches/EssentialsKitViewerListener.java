/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.earth2me.essentials.User
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.earth2me.essentials.User;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

final class EssentialsKitViewerListener
        implements Listener {
    Inventory kitMenuSelector = null;

    EssentialsKitViewerListener() {
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/kit") || e.getMessage().equalsIgnoreCase("/kits") || e.getMessage().equalsIgnoreCase("/essentials:kit") || e.getMessage().equalsIgnoreCase("/essentials:kits") || e.getMessage().equalsIgnoreCase("/essentials:ekit") || e.getMessage().equalsIgnoreCase("/essentials:ekits")) {
            Player pl = e.getPlayer();
            e.setCancelled(true);
            if (Bukkit.getPluginManager().isPluginEnabled("CosmicGlobalKits")) {
                this.openCosmicKitMenuSelector(pl);
            } else {
                this.openEssentialsKitMenu(pl);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals("Choose a kit category...")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            Player pl = (Player) e.getWhoClicked();
            if (e.getRawSlot() == 3) {
                pl.closeInventory();
                this.openEssentialsKitMenu(pl);
            } else if (e.getRawSlot() == 4) {
                pl.closeInventory();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Arkkit.get(), () -> pl.performCommand("vkit"), 1L);
            } else if (e.getRawSlot() == 5) {
                pl.closeInventory();
                pl.performCommand("gkit");
            } else if (e.getRawSlot() == 13) {
                pl.closeInventory();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Arkkit.get(), () -> pl.performCommand("mkit"), 1L);
            }
        }
        if (e.getInventory().getTitle().equals(EssentialsKitViewer.kit_inventory_name)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getCurrentItem() != null) {
                String warp = this.getWarpName(e.getCurrentItem());
                if (warp == null) {
                    return;
                }
                Player pl = (Player) e.getWhoClicked();
                pl.closeInventory();
                Bukkit.getScheduler().runTaskLater(Arkkit.get(), () -> pl.chat("/kit " + warp), 3L);
                //pl.performCommand("kit " + warp);
                //pl.chat("/kit " + warp);
                pl.playSound(pl.getLocation(), Sound.CLICK, 1.0f, 0.75f);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory().getTitle().equals(EssentialsKitViewer.kit_inventory_name)) {
            e.setCancelled(false);
        }
    }

    private void openCosmicKitMenuSelector(Player pl) {
        if (this.kitMenuSelector != null) {
            pl.openInventory(this.kitMenuSelector);
            return;
        }
        boolean masteryKits = Bukkit.getPluginManager().isPluginEnabled("CosmicArmorSets");
        this.kitMenuSelector = Bukkit.createInventory(null, masteryKits ? 18 : 9, "Choose a kit category...");
        this.kitMenuSelector.setItem(3, ItemUtils.buildItem(Material.BED, (short) 0, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rank Kits", ChatColor.GRAY + "Kits unlocked through rank purchases", "", ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "buy.astropvp.net"));
        if (Bukkit.getPluginManager().isPluginEnabled("CosmicEvolutionKits")) {
            this.kitMenuSelector.setItem(4, ItemUtils.buildItem(Material.BLAZE_POWDER, (short) 0, ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Evolution Kits", ChatColor.GRAY + "Powerful kits that have a chance to", ChatColor.GRAY + "evolve each time you redeem them.", "", ChatColor.DARK_PURPLE.toString() + ChatColor.UNDERLINE + "buy.astropvp.net"));
        }
        this.kitMenuSelector.setItem(5, ItemUtils.buildItem(Material.NETHER_STAR, (short) 0, ChatColor.GOLD.toString() + ChatColor.BOLD + "Cosmic Kits", ChatColor.GRAY + "Limited edition kits containing some", ChatColor.GRAY + "of the rarest items in the game.", "", ChatColor.GOLD + ChatColor.UNDERLINE.toString() + "buy.astropvp.net"));
        if (masteryKits) {
            this.kitMenuSelector.setItem(13, ItemUtils.buildItem(Material.DIAMOND, (short) 0, CC.RedB + "Mastery Kits", ChatColor.GRAY + "Godly kits only the most skilled", ChatColor.GRAY + "players are capable of unlocking.", "", ChatColor.RED + ChatColor.UNDERLINE.toString() + "buy.astropvp.net"));
        }
        for (int i = 0; i < this.kitMenuSelector.getSize(); ++i) {
            ItemStack item = this.kitMenuSelector.getItem(i);
            if (item != null && item.getType() != Material.AIR) continue;
            this.kitMenuSelector.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, " ").data(DyeColor.GRAY.getWoolData()).build());
        }
        pl.openInventory(this.kitMenuSelector);
    }

    private void openEssentialsKitMenu(Player pl) {
        User ess_usr = EssentialsKitViewer.getEssentials().getUser(pl);
        ArrayList<ItemStack> warp_items = new ArrayList<ItemStack>();
        for (Map.Entry<String, Material> data : EssentialsKitViewer.kitData.entrySet()) {
            String kit_name = data.getKey();
            double delay_ms = 0.0;
            try {
                delay_ms = EssentialsKitViewer.getEssentials().getKits().getKit(kit_name).containsKey("delay") ? ((Number) EssentialsKitViewer.getEssentials().getKits().getKit(kit_name).get("delay")).doubleValue() : 0.0;
                delay_ms *= 1000.0;
            } catch (Exception err) {
                err.printStackTrace();
            }
            long cooldown_left_ms = (long) (delay_ms - (double) (System.currentTimeMillis() - ess_usr.getKitTimestamp(kit_name)));
            warp_items.add(ItemUtils.buildItem(cooldown_left_ms > 0L ? Material.STAINED_GLASS_PANE : (pl.hasPermission("essentials.kits." + data.getKey().toLowerCase()) ? data.getValue() : Material.STAINED_GLASS_PANE), cooldown_left_ms > 0L ? (short) 4 : (pl.hasPermission("essentials.kits." + data.getKey().toLowerCase()) ? (short) 0 : 14), ChatColor.YELLOW.toString() + ChatColor.BOLD + this.toCamelCase(data.getKey()) + " Kit", pl.hasPermission("essentials.kits." + data.getKey().toLowerCase()) ? ChatColor.RED + (cooldown_left_ms > 0L ? ChatColor.RED + "Cooldown over in " + this.getCooldownString(cooldown_left_ms / 1000L) + "!" : ChatColor.AQUA + "Click to redeem this kit!") : ChatColor.RED + "Purchase a higher rank at " + ChatColor.UNDERLINE + Arkkit.get().getConfig().getString("language.spigot_branding.store_url")));
        }
        pl.playSound(pl.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.67f);
        pl.openInventory(this.buildWarpInventory(warp_items));
    }

    private String getWarpName(ItemStack warp_item) {
        if (warp_item == null || !warp_item.hasItemMeta() || !warp_item.getItemMeta().hasDisplayName()) {
            return null;
        }
        return ChatColor.stripColor(warp_item.getItemMeta().getDisplayName()).split(" ")[0];
    }

    private String getCooldownString(long l) {
        int day = (int) TimeUnit.SECONDS.toDays(l);
        long hours = TimeUnit.SECONDS.toHours(l) - (long) (day * 24L);
        long minute = TimeUnit.SECONDS.toMinutes(l) - TimeUnit.SECONDS.toHours(l) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(l) - TimeUnit.SECONDS.toMinutes(l) * 60L;
        return (day > 0 ? day + "d" : "") + hours + "h " + minute + "m " + second + "s";
    }

    private String toCamelCase(String s) {
        String[] parts = s.split(" ");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = part != null && part.trim().length() > 0 ? camelCaseString + this.toProperCase(part) : camelCaseString + part + " ";
        }
        return camelCaseString.contains(" ") ? camelCaseString.substring(0, camelCaseString.length() - 1) : camelCaseString;
    }

    private String toProperCase(String s) {
        String temp = s.trim();
        String spaces = "";
        if (temp.length() != s.length()) {
            char startCharIndex = s.charAt(temp.indexOf(0));
            spaces = s.substring(0, startCharIndex);
        }
        temp = temp.substring(0, 1).toUpperCase() + spaces + temp.substring(1).toLowerCase() + " ";
        return temp;
    }

    private Inventory buildWarpInventory(List<ItemStack> warp_items) {
        int size = this.getInvSize(warp_items.size());
        Inventory inv = Bukkit.createInventory(null, size, EssentialsKitViewer.kit_inventory_name);
        for (ItemStack is : warp_items) {
            inv.addItem(is);
        }
        return inv;
    }

    private int getInvSize(int amount) {
        int inv_size;
        for (inv_size = 9; inv_size < amount && inv_size < 81; inv_size += 9) {
        }
        return inv_size;
    }
}

