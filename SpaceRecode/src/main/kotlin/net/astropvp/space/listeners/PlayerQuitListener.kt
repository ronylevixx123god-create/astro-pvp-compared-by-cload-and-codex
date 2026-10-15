package net.astropvp.space.listeners

import net.astropvp.space.SpacePlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val sp = SpacePlugin.activeSpaceDataManager.get(player) ?: return
        sp.start = System.currentTimeMillis() + System.currentTimeMillis()
        //sp.teleportAndExit()
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        val player = event.player
        val sp = SpacePlugin.activeSpaceDataManager.get(player) ?: return
        sp.start = System.currentTimeMillis() + System.currentTimeMillis()
        //sp.teleportAndExit()
    }

}