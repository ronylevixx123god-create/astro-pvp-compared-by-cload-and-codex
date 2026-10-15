package net.astropvp.space.listeners

import net.astropvp.space.SpaceAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleFlightEvent

class DisableFlightListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onFlight(event: PlayerToggleFlightEvent) {
        val player = event.player
        if (player.isOp) {
            return
        }
        if (!SpaceAPI.WORLDS.contains(event.player.world.name)) {
            return
        }
        if (event.isFlying) {
            player.allowFlight = false
            event.isCancelled = true
        }
    }
}