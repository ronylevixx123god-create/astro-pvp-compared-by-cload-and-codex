package me.astropvp.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ScrollerInventory {
    public ArrayList<Inventory> pages = new ArrayList();
    public UUID id = UUID.randomUUID();
    public int currpage = 0;
    public static HashMap<UUID, ScrollerInventory> users = new HashMap();
    public static final String nextPageName = ChatColor.AQUA + "Next Page";
    public static final String previousPageName = ChatColor.AQUA + "Previous Page";

    public ScrollerInventory(ArrayList<ItemStack> items, String name, Player p) {
        Inventory page = this.getBlankPage(name);
        for (int i = 0; i < items.size(); ++i) {
            if (page.firstEmpty() == 46) {
                this.pages.add(page);
                page = this.getBlankPage(name);
                page.addItem(items.get(i));
                continue;
            }
            page.addItem(items.get(i));
        }
        this.pages.add(page);
        p.openInventory(this.pages.get(this.currpage));
        users.put(p.getUniqueId(), this);
    }

    private Inventory getBlankPage(String name) {
        Inventory page = Bukkit.createInventory(null, 54, name);
        ItemStack nextpage = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta meta = nextpage.getItemMeta();
        meta.setDisplayName(nextPageName);
        nextpage.setItemMeta(meta);
        ItemStack prevpage = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 2);
        meta = prevpage.getItemMeta();
        meta.setDisplayName(previousPageName);
        prevpage.setItemMeta(meta);
        page.setItem(53, nextpage);
        page.setItem(45, prevpage);
        return page;
    }
}
