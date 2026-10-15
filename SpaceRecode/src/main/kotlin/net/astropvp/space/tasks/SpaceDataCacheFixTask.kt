package net.astropvp.space.tasks

import net.astropvp.space.SpaceAPI
import net.astropvp.space.SpacePlugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class SpaceDataCacheFixTask : BukkitRunnable() {

    override fun run() {
        for (spacePlayer in SpacePlugin.activeSpaceDataManager.spacePlayers) {
            val player = Bukkit.getPlayer(spacePlayer.key) ?: continue
            if(!SpaceAPI.WORLDS.contains(player.world.name)) {
                SpacePlugin.activeSpaceDataManager.remove(spacePlayer.key)
            }
        }
    }

}