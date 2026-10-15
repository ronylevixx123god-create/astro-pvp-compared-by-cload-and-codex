package org.arkhamnetwork.Arkkit.patches.trading;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BetterTradeListener implements Listener {
    private BetterTrade instance;

    public BetterTradeListener(BetterTrade trade) {
        this.instance = trade;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (this.instance.getCurrentTrade(event.getPlayer()) != null) {
            event.setCancelled(true);
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player pl = (Player)event.getEntity();
            if (this.instance.getCurrentTrade(pl) != null) {
                pl.closeInventory();
            }
        }

    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        PlayerTrade trade = this.instance.getCurrentTrade((Player)event.getPlayer());
        if (trade != null && !event.getInventory().getTitle().contains("  ")) {
            event.setCancelled(true);
            Bukkit.getLogger().info("Player " + event.getPlayer().getName() + " tried to open " + event.getInventory().getTitle() + " while in a trade!?");
        }

    }
}
