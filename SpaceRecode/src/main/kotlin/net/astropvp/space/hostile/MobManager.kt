package net.astropvp.space.hostile

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

object MobManager {

    val activeEntities: MutableMap<Int, Pair<String, LivingEntity>> = mutableMapOf()

    fun add(mobType: String, entity: LivingEntity) {
        activeEntities[entity.entityId] = Pair(mobType, entity)
    }

    fun remove(entity: LivingEntity) {
        activeEntities.remove(entity.entityId)
    }

    fun shouldSpawn(location: Location): Boolean {
        var playersInChunk = 0
        var mobsAmount = 0
        for (entity in location.world.getNearbyEntities(location, 16.0, 16.0, 16.0)) {
            if (entity.type == EntityType.PLAYER) {
                playersInChunk++
            } else if (entity is LivingEntity) {
                mobsAmount++
            }
        }
        if (playersInChunk == 0) {
            return false
        }
        if (mobsAmount >= 8) {
            return false
        }
        return true
    }

    fun isActiveEntity(entity: LivingEntity): Boolean {
        return activeEntities.containsKey(entity.entityId)
    }

    fun isActiveEntity(entityId: Int): Boolean {
        return activeEntities.containsKey(entityId)
    }

}