package net.astropvp.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InvseeManager {

    private Inventory inventory;
    private int size;
    private String title;
    private ItemStack placeholder;

    public InvseeManager(Player p, Player t) {
        this.size = 45;
        this.title = t.getName() + "'s Inventory";
        this.placeholder = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta im = placeholder.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f"));
        placeholder.setItemMeta(im);
        ItemStack[] items = t.getInventory().getContents();
        this.inventory = Bukkit.createInventory(p, size, title);
        for (int i = 0; i < 36; i++) {
            if (items[i] != null)
                inventory.setItem(i, items[i]);
        }
        inventory.setItem(36, placeholder);
        inventory.setItem(37, placeholder);
        inventory.setItem(40, placeholder);
        PlayerInventory oldInv = t.getInventory();
        if (oldInv.getHelmet() != null)
            inventory.setItem(38, oldInv.getHelmet());
        if (oldInv.getChestplate() != null)
            inventory.setItem(39, oldInv.getChestplate());
        if (oldInv.getLeggings() != null)
            inventory.setItem(41, oldInv.getLeggings());
        if (oldInv.getBoots() != null)
            inventory.setItem(42, oldInv.getBoots());
        inventory.setItem(43, placeholder);
        inventory.setItem(44, placeholder);
    }


    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }
}
