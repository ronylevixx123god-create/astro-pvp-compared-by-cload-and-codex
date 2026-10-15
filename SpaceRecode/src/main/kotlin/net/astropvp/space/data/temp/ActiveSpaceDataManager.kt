package net.astropvp.space.data.temp

import org.bukkit.entity.Player
import java.util.UUID

class ActiveSpaceDataManager {

    val spacePlayers: MutableMap<UUID, ActiveSpaceData> = mutableMapOf()

    fun getOrCreate(player: Player): ActiveSpaceData {
        if (spacePlayers.containsKey(player.uniqueId)) {
            return spacePlayers[player.uniqueId]!!
        }
        val sp = ActiveSpaceData(player.uniqueId)
        spacePlayers[player.uniqueId] = sp
        return sp
    }

    fun collectAll(): MutableSet<UUID> {
        return spacePlayers.keys
    }

    fun remove(player: Player) {
        spacePlayers.remove(player.uniqueId)
    }

    fun remove(uuid: UUID) {
        spacePlayers.remove(uuid)
    }

    fun findLongestSurvivor(): ActiveSpaceData? {
        return spacePlayers.values.minByOrNull { it.start }
    }

    fun get(player: Player): ActiveSpaceData? {
        return spacePlayers[player.uniqueId]
    }

}