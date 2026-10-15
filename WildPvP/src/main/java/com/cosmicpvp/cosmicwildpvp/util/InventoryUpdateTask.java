package com.cosmicpvp.cosmicwildpvp.util;

import com.cosmicpvp.cosmicwildpvp.WildPvPPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class InventoryUpdateTask extends BukkitRunnable
{
    private Player player;
    private Inventory inventory;

    public InventoryUpdateTask(final Player player, final Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
        this.player.openInventory(inventory);
        this.runTaskTimer((Plugin) WildPvPPlugin.getInstance(), 60L, 60L);
    }

    public void run() {
        if (!this.player.isOnline()) {
            this.cancel();
            return;
        }
        if (this.player.getOpenInventory() == null) {
            this.cancel();
            return;
        }
        if (!this.player.getOpenInventory().getTopInventory().getTitle().equals(this.inventory.getTitle())) {
            this.cancel();
            return;
        }
        this.inventory.setContents(this.getUpdatedContents());
    }

    public abstract ItemStack[] getUpdatedContents();
}
