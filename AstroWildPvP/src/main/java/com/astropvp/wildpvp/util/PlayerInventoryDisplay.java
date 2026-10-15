package com.astropvp.wildpvp.util;

import com.google.common.collect.Lists;
import java.util.ListIterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PlayerInventoryDisplay {
    private Inventory inventory;

    public PlayerInventoryDisplay(Player player) {
        this.inventory = Bukkit.createInventory((InventoryHolder)null, 54, ChatColor.GREEN + player.getName() + ChatColor.GREEN + "'s Inventory");
        this.inventory.setContents(player.getInventory().getContents());
        this.inventory.setItem(45, KrizzUtil.createItem(Material.ARROW, "&a&lReturn to Queue", "", "&7Click here to return to the", "&7Wild PvP queue menu."));
        ListIterator slotIter = Lists.newArrayList(new Integer[]{51, 50, 48, 47}).listIterator();

        while(slotIter.hasNext()) {
            this.inventory.setItem((Integer)slotIter.next(), player.getInventory().getArmorContents()[slotIter.nextIndex() - 1]);
        }

    }

    public void show(Player viewer) {
        viewer.closeInventory();
        viewer.openInventory(this.inventory);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
