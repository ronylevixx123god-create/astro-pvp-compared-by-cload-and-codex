/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicFund.CosmicFundAPI
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.ClickType
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.cosmicpvp.CosmicGlobalKits.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.List;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitUser;
import me.cosmicpvp.CosmicGlobalKits.utils.GlobalCooldowns;
import me.cosmicpvp.CosmicGlobalKits.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventoryListener
implements Listener {
    @EventHandler
    public void onKitSelect(InventoryClickEvent e) {
        Player pl;
        if (e.getInventory().getName().equals("Astro Kits")) {
            pl = (Player)e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            pl.updateInventory();
            if (!e.getInventory().equals((Object)pl.getOpenInventory().getTopInventory())) {
                return;
            }
            if (e.getCurrentItem() != null) {
                GlobalKitUser user = CosmicGlobalKits.getPlugin().getGlobalKitUser(pl.getUniqueId());
                if (user == null || user.isLoadingData()) {
                    pl.sendMessage(CC.Red + "Please wait while your Global Kit data loads...");
                    pl.closeInventory();
                    return;
                }
                ItemStack kit_item = e.getCurrentItem();
                final GlobalKits gkit = GlobalKits.getGKit(kit_item);
                if (gkit == null) {
                    return;
                }
                if (!GlobalKits.canUseKit(pl, gkit) || e.getClick() == ClickType.RIGHT) {
                    if (e.getClick() != ClickType.RIGHT) {
                        pl.sendMessage(ChatColor.RED.toString() + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You have " + (Object)ChatColor.UNDERLINE + "not" + (Object)ChatColor.RED + " unlocked that Astro Kit!");
                        pl.sendMessage((Object)ChatColor.GRAY + "Unlock access by defeating a Fallen Hero purchased at " + (Object)ChatColor.UNDERLINE + CosmicGlobalKits.getPlugin().getConfig().getString("store_url"));
                        pl.sendMessage((Object)ChatColor.GRAY + "** You can preview the contents of this kit by right-clicking it in the /gkit menu.");
                        pl.closeInventory();
                    } else {
                        pl.closeInventory();
                        Player finalPl = pl;
                        Bukkit.getScheduler().runTaskLater((Plugin)CosmicGlobalKits.getPlugin(), new Runnable(){

                            @Override
                            public void run() {
                                finalPl.playSound(finalPl.getLocation(), Sound.BAT_TAKEOFF, 0.8f, 0.9f);
                                finalPl.openInventory(InventoryListener.this.getKitPreview(gkit, finalPl));
                            }
                        }, 1L);
                    }
                    return;
                }
                if (Bukkit.getPluginManager().isPluginEnabled("CosmicFund") && gkit != GlobalKits.Masterbuilder && !pl.isOp()) {
                    pl.sendMessage(ChatColor.RED.toString() + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "Your planet has not yet unlocked access to this type of /gkit. To unlock, your planet needs to reach the " + (Object)ChatColor.UNDERLINE + "/fund" + (Object)ChatColor.RED + " goal.");
                    pl.sendMessage((Object)ChatColor.GRAY + "* Until the goal is met, NO PLAYERS can redeem these kits.");
                    return;
                }
                if (GlobalCooldowns.hasCooldown(pl, gkit)) {
                    pl.sendMessage((Object)ChatColor.YELLOW + "You " + (Object)ChatColor.UNDERLINE + "cannot" + (Object)ChatColor.YELLOW + " use that Astro Kit for " + GlobalCooldowns.getCooldownString((int)(GlobalCooldowns.getCooldownMS(pl, gkit) / 1000L)));
                    pl.closeInventory();
                    return;
                }
                for (ItemStack i : CosmicGlobalKitsAPI.getGlobalKitItems(gkit, pl.getName())) {
                    ItemUtils.giveItemToPlayer(pl, i);
                }
                GlobalCooldowns.startCooldown(pl, gkit);
                pl.closeInventory();
            }
        }
        if (e.getInventory().getName().equals("Astro Kit Preview")) {
            pl = (Player)e.getWhoClicked();
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            pl.sendMessage(ChatColor.RED.toString() + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot withdraw me.astropvp.cratekeys.items from a kit preview.");
            pl.sendMessage((Object)ChatColor.GRAY + "Unlock access by defeating a Fallen Hero purchased at:");
            pl.sendMessage((Object)ChatColor.GRAY + "" + (Object)ChatColor.UNDERLINE + CosmicGlobalKits.getPlugin().getConfig().getString("store_url"));
            pl.closeInventory();
        }
    }

    private Inventory getKitPreview(GlobalKits gkit, Player pl) {
        List<ItemStack> items = CosmicGlobalKitsAPI.getGlobalKitItems(gkit, pl.getName(), true);
        Inventory i = Bukkit.createInventory(null, (int)Math.max(9, (int)(Math.ceil((double)items.size() / 9.0) * 9.0)), (String)"Astro Kit Preview");
        for (ItemStack is : items) {
            i.addItem(new ItemStack[]{is});
        }
        for (int x = 0; x < i.getSize(); ++x) {
            if (i.getItem(x) != null && i.getItem(x).getType() != Material.AIR) continue;
            i.setItem(x, ItemUtils.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", ""));
        }
        return i;
    }
}

