package net.astropvp.core.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class DungeonInventoryEvent implements Listener {
    @EventHandler
    public void PlayerChangedWorld(PlayerChangedWorldEvent e) {
        World world = e.getPlayer().getWorld();
        if (world.getName().startsWith("dungeon_")) {
            world.setGameRuleValue("keepInventory", "true");
        }
    }

}
