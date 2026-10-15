/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.EnchantingInventory
 *  org.bukkit.me.astropvp.cratekeys.inventory.InventoryView
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.permissions.Permissible
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package com.rit.sucy.enchanting;

import com.rit.sucy.EUpdateTask;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.config.LanguageNode;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TableTask
extends BukkitRunnable {
    final ItemStack p = new ItemStack(Material.BOOK);
    final ItemStack p2 = new ItemStack(Material.BOOK);
    final String cantEnchant;
    Player player;
    Plugin plugin;
    ItemStack placeholder;
    ItemStack stored;

    public TableTask(Plugin plugin, Player player) {
        this.player = player;
        this.plugin = plugin;
        List enchantable = plugin.getConfig().getStringList(LanguageNode.TABLE_ENCHANTABLE.getFullPath());
        List unenchantable = plugin.getConfig().getStringList(LanguageNode.TABLE_UNENCHANTABLE.getFullPath());
        this.cantEnchant = ((String)unenchantable.get(1)).replace('&', '\u00a7');
        ItemMeta meta = this.p.getItemMeta();
        meta.setDisplayName(((String)enchantable.get(0)).replace('&', '\u00a7'));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(((String)enchantable.get(1)).replace('&', '\u00a7'));
        meta.setLore(lore);
        this.p.setItemMeta(meta);
        lore.clear();
        lore.add(this.cantEnchant);
        meta.setLore(lore);
        meta.setDisplayName(((String)unenchantable.get(0)).replace('&', '\u00a7'));
        this.p2.setItemMeta(meta);
        this.runTaskTimer(plugin, 0L, 1L);
    }

    public void restore() {
        if (this.placeholder != null) {
            this.placeholder.setType(this.stored.getType());
            this.placeholder.setItemMeta(this.stored.getItemMeta());
            this.placeholder.setAmount(this.stored.getAmount());
            this.placeholder = null;
        }
    }

    public void run() {
        InventoryView view = this.player.getOpenInventory();
        if (view == null) {
            this.cancel();
            return;
        }
        EnchantingInventory inv = null;
        inv = (EnchantingInventory)view.getTopInventory();
        if (!(this.placeholder == null || inv.getItem() != null && inv.getItem().hasItemMeta() && inv.getItem().getItemMeta().hasDisplayName() && inv.getItem().getItemMeta().getDisplayName().equals(this.placeholder.getItemMeta().getDisplayName()))) {
            this.restore();
            new EUpdateTask(this.plugin, this.player);
        }
        if (inv.getItem() != null && inv.getItem().getType() != Material.AIR && this.placeholder == null) {
            this.stored = inv.getItem().clone();
            this.placeholder = this.createPlaceholder(inv.getItem(), this.stored);
            inv.setItem(this.placeholder);
            this.placeholder = inv.getItem();
        }
    }

    private ItemStack createPlaceholder(ItemStack item, ItemStack storedItem) {
        if (this.canEnchant(item)) {
            item.setType(this.p.getType());
            item.setItemMeta(this.p.getItemMeta());
        } else {
            item.setType(this.p2.getType());
            item.setItemMeta(this.p2.getItemMeta());
        }
        item.setAmount(1);
        List lore = item.getItemMeta().getLore();
        if (storedItem.hasItemMeta() && storedItem.getItemMeta().hasDisplayName()) {
            lore.add(storedItem.getItemMeta().getDisplayName());
        } else {
            lore.add((Object)ChatColor.GRAY + storedItem.getType().name().toLowerCase().replace("_", " "));
        }
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private boolean canEnchant(ItemStack item) {
        if (EnchantmentAPI.getAllValidEnchants(item, (Permissible)this.player).size() == 0) {
            return false;
        }
        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            return false;
        }
        return EnchantmentAPI.getEnchantments(item).size() <= 0;
    }
}

