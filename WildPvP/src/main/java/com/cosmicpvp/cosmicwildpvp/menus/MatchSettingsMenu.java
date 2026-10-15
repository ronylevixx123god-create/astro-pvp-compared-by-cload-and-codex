package com.cosmicpvp.cosmicwildpvp.menus;

import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import com.cosmicpvp.cosmicwildpvp.settings.CagesSetting;
import com.cosmicpvp.cosmicwildpvp.util.Menu;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import com.google.common.collect.Lists;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MatchSettingsMenu implements Menu
{
    private Inventory inventory;
    private WildPvPPlugin plugin;
    boolean cagesEnabled;
    boolean doublesEnabled;

    public MatchSettingsMenu(final Player player, final WildPvPPlugin plugin) {
        this.inventory = Bukkit.createInventory((InventoryHolder)this, 9, "Match Settings");
        this.cagesEnabled = false;
        this.doublesEnabled = false;
        this.plugin = plugin;
        this.inventory.setItem(4, this.getConfirmQueue());
    }

    public void update(final Player player) {
        this.inventory.setItem(22, this.getConfirmQueue());
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public void onClick(final Player player, final int slot, final ClickType type, final InventoryClickEvent event) {
        if (slot == 4) {
            event.setCancelled(true);
            this.plugin.getQueueManager().createNewQueue(player, this.cagesEnabled);
            player.closeInventory();
        }
    }

    private ItemStack getCagesClickable() {
        return new CagesSetting().getMatchItem(this.cagesEnabled);
    }

    private ItemStack getConfirmQueue() {
        final ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate("&a&lConfirm Queue"));
        final List<String> lore = Lists.newArrayList();
        lore.add(CC.translate("&7Click here to accept and confirm the queue."));
        meta.setLore((List)lore);
        item.setItemMeta(meta);
        return item;
    }
}
