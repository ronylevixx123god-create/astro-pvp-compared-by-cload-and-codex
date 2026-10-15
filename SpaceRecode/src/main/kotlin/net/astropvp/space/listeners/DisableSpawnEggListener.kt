package net.astropvp.space.listeners

import com.cosmicpvp.cosmicutils.utils.CC
import net.astropvp.space.SpaceAPI
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class DisableSpawnEggListener : Listener {
    @EventHandler
    fun spawnMobEvent(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_BLOCK && e.material == Material.MONSTER_EGG) {
            if (SpaceAPI.WORLDS.contains(e.player.world.name)) {
                e.isCancelled = true
                e.player.sendMessage(CC.translate("&c&l(!)&c You cannot use spawn eggs in the space."))
            }
        }
    }
}