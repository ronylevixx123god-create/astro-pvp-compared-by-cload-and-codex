package com.cosmicpvp.cosmicwildpvp.util;

import java.util.ListIterator;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerInventoryDisplay
{
    private Inventory inventory;

    public PlayerInventoryDisplay(final Player player) {
        (this.inventory = Bukkit.createInventory((InventoryHolder)null, 54, ChatColor.GREEN + player.getName() + ChatColor.GREEN + "'s Inventory")).setContents(player.getInventory().getContents());
        this.inventory.setItem(45, BryUtil.createItem(Material.ARROW, "&a&lReturn to Queue", "", "&7Click here to return to the", "&7Wild PvP queue menu."));
        final ListIterator<Integer> slotIter = Lists.newArrayList(51, 50, 48, 47).listIterator();
        while (slotIter.hasNext()) {
            this.inventory.setItem((int)slotIter.next(), player.getInventory().getArmorContents()[slotIter.nextIndex() - 1]);
        }
    }

    public void show(final Player viewer) {
        viewer.closeInventory();
        viewer.openInventory(this.inventory);
    }
}
