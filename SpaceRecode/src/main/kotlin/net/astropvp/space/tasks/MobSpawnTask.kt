package net.astropvp.space.tasks

import net.astropvp.space.hostile.MobManager
import net.astropvp.space.hostile.MobType
import net.astropvp.space.hostile.MobTypeRegister
import net.astropvp.space.struct.LocationManager
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.particle.ParticleEffect

class MobSpawnTask : BukkitRunnable() {

    override fun run() {
        for (location in LocationManager.locations) {
            if (MobManager.shouldSpawn(location)) {
                try {
                    ParticleEffect.SPELL_WITCH.display(location)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    if (Math.random() < 0.1) {
                        val hostile = MobTypeRegister.get("SPACE_ZOMBIE_KING") ?: return
                        hostile.spawn(location)
                    } else {
                        val hostile = MobTypeRegister.get("SPACE_ZOMBIE") ?: return
                        hostile.spawn(location)
                    }
                } catch (e: NoClassDefFoundError) {
                    // Log the exact issue
                    println("Failed to spawn mob: ${e.message}")
                    e.printStackTrace()
                } catch (e: Exception) {
                    // Log any other exceptions
                    println("An error occurred while spawning mobs: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

}