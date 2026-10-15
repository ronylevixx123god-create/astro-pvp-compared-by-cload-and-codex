package com.astropvp.wildpvp.util;

import com.astropvp.wildpvp.AstroWildPvP;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class InventoryUpdateTask extends BukkitRunnable {
    private Player player;
    private Inventory inventory;

    public InventoryUpdateTask(Player player, Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
        this.player.openInventory(inventory);
        this.runTaskTimer(AstroWildPvP.getInstance(), 0L, 20L);
    }

    public void run() {
        try {
            if (!this.player.isOnline()) {
                this.cancel();
            } else if (this.player.getOpenInventory() == null) {
                this.cancel();
            } else if (this.inventory == null) {
                this.cancel();
            } else if (!this.player.getOpenInventory().getTopInventory().getTitle().equals(this.inventory.getTitle())) {
                this.cancel();
            } else {
                this.inventory.setContents(this.getUpdatedContents());
            }
        }catch (Exception ignored) {}
    }
    public abstract ItemStack[] getUpdatedContents();
}
