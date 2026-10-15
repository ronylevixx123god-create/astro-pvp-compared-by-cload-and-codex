package net.astropvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DupePatch implements Listener {
    @EventHandler
    public void closeGUIWhenWorldSwitch(InventoryOpenEvent e) {
        if (e.getPlayer() == null) return;
        if (e.getPlayer().getLocation().getWorld().getName().equals("world_duels")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void closeGUIInWorld(PlayerTeleportEvent e) {
        if (e.getPlayer() == null) return;
        if (e.getTo().getWorld().getName().equals("world_duels")) {
            e.getPlayer().getOpenInventory().close();
        }
    }
}
