package org.arkhamnetwork.enchantments.listeners;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ReskinListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryOpen(final InventoryOpenEvent e) {
        this.convertBooks(e.getInventory());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPickupItem(final PlayerPickupItemEvent e) {
        if (EnchantmentBook.isOldEnchantmentBook(e.getItem().getItemStack())) {
            e.getItem().setItemStack(this.convertBook(e.getItem().getItemStack()));
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        this.convertBooks((Inventory) e.getPlayer().getInventory());
    }

    private void convertBooks(final Inventory inv) {
        for (int n = 0; n < inv.getSize(); ++n) {
            ItemStack i = inv.getItem(n);
            if (EnchantmentBook.isOldEnchantmentBook(i)) {
                i = EnchantmentBook.getEnchantmentBook(EnchantmentBook.getOldEnchantment(i), EnchantmentBook.getOldEnchantmentLevel(i), EnchantmentBook.getOldEnchantmentBookSuccessRate(i), EnchantmentBook.getOldEnchantmentBookDestructionRate(i));
                inv.setItem(n, i);
            }
        }
    }

    private ItemStack convertBook(final ItemStack i) {
        return EnchantmentBook.getEnchantmentBook(EnchantmentBook.getOldEnchantment(i), EnchantmentBook.getOldEnchantmentLevel(i), EnchantmentBook.getOldEnchantmentBookSuccessRate(i), EnchantmentBook.getOldEnchantmentBookDestructionRate(i));
    }

    private boolean notColored(final ItemStack i) {
        return i.hasItemMeta() && i.getItemMeta().hasLore() && i.getItemMeta().getLore().get(0).startsWith(ChatColor.GRAY.toString());
    }

    private ItemStack colorEnchantments(final ItemStack i) {
        ItemStack newItem = i.clone();
        for (final Map.Entry<CustomEnchantment, Integer> data : EnchantmentAPI.getEnchantments(i).entrySet()) {
            newItem = data.getKey().addToItem(newItem, (int) data.getValue());
        }
        return newItem;
    }
}
