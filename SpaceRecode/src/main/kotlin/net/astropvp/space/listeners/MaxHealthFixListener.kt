package net.astropvp.space.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class MaxHealthFixListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if(event.player.maxHealth > 20) {
            event.player.maxHealth = 20.0
        }
    }
}