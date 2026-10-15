/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package net.astropvp.core.titles.listeners;

import net.astropvp.core.titles.PlayerTitleAPI;
import net.astropvp.core.titles.sql.TitleDatabaseManager;
import net.astropvp.core.titles.utils.TitleInterface;
import net.astropvp.core.titles.utils.TitleUtil;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TitleInventoryListener
implements Listener {
    private final TitleDatabaseManager dbM;

    public TitleInventoryListener(TitleDatabaseManager dm) {
        this.dbM = dm;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().startsWith(ChatColor.BLACK + "Unlocked Player Titles")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            ItemStack current = event.getCurrentItem();
            if (current != null && current.hasItemMeta() && current.getItemMeta().hasLore()) {
                String title = event.getInventory().getTitle();
                AtomicInteger page = new AtomicInteger(1);
                if (title.contains("(")) {
                    try {
                        page.set(Integer.parseInt(title.split("\\(")[1].split("/")[0]));
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (current.getType() == Material.ARROW) {
                    if (current.getItemMeta().getDisplayName().contains("Next")) {
                        page.incrementAndGet();
                    } else if (current.getItemMeta().getDisplayName().contains("Previous")) {
                        page.decrementAndGet();
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(AstroCore.getInstance(), () -> {
                        TitleInterface.openInterface((Player)event.getWhoClicked(), page.get());
                        ((Player)event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.BAT_TAKEOFF, 1.0f, 1.4f);
                    }, 1L);
                    return;
                }
                final HumanEntity clicker = event.getWhoClicked();
                String _clickedtitle = TitleUtil.removeColorCodes(current.getItemMeta().getDisplayName().replace("\u00a7", "&"));
                List itemlore = current.getItemMeta().getLore();
                String format_clickedtitle = "none";
                Iterator<String> iterator = PlayerTitleAPI.getUnlockedTitles(clicker.getUniqueId()).iterator();
                while (iterator.hasNext()) {
                    String s;
                    String fs = s = iterator.next();
                    if (!_clickedtitle.equalsIgnoreCase(fs = fs.replace("_", " "))) continue;
                    format_clickedtitle = s;
                    break;
                }
                if (itemlore.contains("\u00a76Equipped")) {
                    this.dbM.setEquippedTitle(clicker.getUniqueId(), "none");
                    clicker.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You have un-equipped the " + ChatColor.GOLD + ChatColor.UNDERLINE + format_clickedtitle + ChatColor.RED + " title.");
                    clicker.sendMessage(ChatColor.GRAY + "Change this at any time in the /title menu.");
                }
                if (itemlore.contains("\u00a7fUnlocked")) {
                    this.dbM.setEquippedTitle(clicker.getUniqueId(), format_clickedtitle);
                    clicker.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!) " + ChatColor.YELLOW + "You have equipped the " + ChatColor.GOLD + ChatColor.UNDERLINE + format_clickedtitle + ChatColor.YELLOW + " title.");
                    clicker.sendMessage(ChatColor.GRAY + "Your new title will now appear when you type in chat" + (AstroCore.hcf ? "" : ", and in your character's overhead name."));
                }
                Bukkit.getScheduler().runTaskLater(AstroCore.getInstance(), new Runnable(){

                    @Override
                    public void run() {
                        clicker.closeInventory();
                    }
                }, 1L);
            }
        }
    }
}

