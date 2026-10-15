/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.ClickType
 *  org.bukkit.me.astropvp.cratekeys.inventory.Inventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.recolance.auction.menu;

import me.recolance.auction.Auction;
import me.recolance.auction.util.Util;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MenuUtil {
    public static boolean isCancellableClick(Inventory inventory, int slot, ClickType click) {
        if (slot < inventory.getSize()) {
            return true;
        }
        return click != ClickType.LEFT && click != ClickType.RIGHT;
    }

    public static void displayMenuInvalidity(final int slot, final Inventory inventory, final ItemStack originalItem, String message) {
        message = Util.setStringColors(message);
        if (originalItem.getType() == Material.STAINED_GLASS_PANE) {
            return;
        }
        inventory.setItem(slot, new MenuButton().type(Material.STAINED_GLASS_PANE).durability(14).name("&c" + message).get());
        new BukkitRunnable() {

            public void run() {
                ItemStack item = inventory.getItem(slot);
                if (item != null && inventory.getItem(slot).getType() == Material.STAINED_GLASS_PANE) {
                    inventory.setItem(slot, originalItem);
                }
            }
        }.runTaskLater((Plugin) Auction.plugin, 20L);
    }

    public static void displayMenuInfo(final int slot, final Inventory inventory, final ItemStack originalItem, String message) {
        message = Util.setStringColors(message);
        if (originalItem.getType() == Material.STAINED_GLASS_PANE) {
            return;
        }
        inventory.setItem(slot, new MenuButton().type(Material.STAINED_GLASS_PANE).durability(4).name("&e" + message).get());
        new BukkitRunnable() {

            public void run() {
                ItemStack item = inventory.getItem(slot);
                if (item != null && inventory.getItem(slot).getType() == Material.STAINED_GLASS_PANE) {
                    inventory.setItem(slot, originalItem);
                }
            }
        }.runTaskLater((Plugin) Auction.plugin, 20L);
    }
}

