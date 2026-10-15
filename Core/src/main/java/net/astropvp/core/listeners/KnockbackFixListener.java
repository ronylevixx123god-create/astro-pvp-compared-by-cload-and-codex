package net.astropvp.core.listeners;

import me.astropvp.factionlogs.struct.impl.PlayerJoin;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class KnockbackFixListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ((CraftPlayer)event.getPlayer()).setMaximumNoDamageTicks(19);
    }
}
