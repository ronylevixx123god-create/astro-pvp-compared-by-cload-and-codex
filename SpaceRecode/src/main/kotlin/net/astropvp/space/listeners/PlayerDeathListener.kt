package net.astropvp.space.listeners

import net.astropvp.space.SpaceAPI
import net.astropvp.space.SpacePlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if(SpaceAPI.WORLDS.contains(event.entity.world.name)) {
            val xp = event.entity.exp
            event.drops.clear()
            event.droppedExp = 0

            Bukkit.getScheduler().runTaskLater(SpacePlugin.instance, { event.entity.exp = xp}, 5L)
        }
    }
}