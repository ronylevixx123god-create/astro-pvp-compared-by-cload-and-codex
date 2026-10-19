//
// Decompiled by Procyon v0.5.36
//

package net.astropvp.menu;

import com.decimatemc.blackmarketauction.BlackMarketAuction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import net.astropvp.util.Utils;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

public class AuctionLootMenu implements Menu
{
    private final Inventory inventory;

    public AuctionLootMenu() {
        inventory = Bukkit.createInventory(this, 54, "BAH Loot Table");
        updateInventory();
    }

    public void updateInventory() {
        inventory.clear();
        java.util.List<ItemStack> items = BlackMarketAuction.get().getAuctionManager().getPossibleItems();
        for (int i = 0; i < Math.min(items.size(), inventory.getSize()); i++) {
            inventory.setItem(i, items.get(i));
        }
    }

    @Override
    public void onClick(final Player player, final InventoryClickEvent event) {
        event.setCancelled(true);
        if (!player.isOp() && !player.hasPermission("decimatebah.admin")) return;
        if (event.getClickedInventory() == null) return;
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            if (BlackMarketAuction.get().getAuctionManager().getPossibleItems().size() >= 54) {
                player.sendMessage("The loot editor supports up to 54 items; remove an item first.");
                return;
            }
            BlackMarketAuction.get().getAuctionManager().addPossibleItem(item);
        } else if (event.getClickedInventory().equals(inventory)) {
            BlackMarketAuction.get().getAuctionManager().removePossibleItem(item);
        }
        updateInventory();
    }

    public Inventory getInventory() { return inventory; }
}
