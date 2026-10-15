/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package net.astropvp.core.cosmictoggles.listeners;

import net.astropvp.core.AstroCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener
implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (AstroCore.getInstance().getToggleManager().getScoreboardToggles().contains(event.getPlayer().getUniqueId())) {
            AstroCore.getInstance().getToggleManager().clearScoreboard(event.getPlayer());
        }
    }
}

