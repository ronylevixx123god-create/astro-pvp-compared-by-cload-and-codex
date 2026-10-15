package net.astropvp.space.listeners

import net.astropvp.space.SpaceAPI
import net.astropvp.space.SpacePlugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if(SpaceAPI.WORLDS.contains(event.player.location.world.name)) {
            Bukkit.getScheduler().runTaskLater(SpacePlugin.instance, {
                val spacePlayer = SpacePlugin.activeSpaceDataManager.getOrCreate(event.player)
                spacePlayer.teleportAndExit()
            }, 3L)
        }
    }
}