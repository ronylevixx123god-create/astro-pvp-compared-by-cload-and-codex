package net.astropvp.space.tasks

import net.astropvp.space.SpaceAPI
import net.astropvp.space.hostile.MobManager
import net.astropvp.space.utils.msg
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class ClearMobsTask : BukkitRunnable() {

    override fun run() {
        MobManager.activeEntities.forEach {
            it.value.second.remove()
        }
        MobManager.activeEntities.clear()

        SpaceAPI.WORLDS.forEach { Bukkit.getWorld(it).entities.filter { type -> type !is Player } .forEach { entity -> entity.remove() }}
        SpaceAPI.WORLDS.forEach { Bukkit.getWorld(it).players.forEach { player -> player.msg("&d&l(!) &dAll space mobs have been cleared, new ones will spawn soon")}}
    }

}