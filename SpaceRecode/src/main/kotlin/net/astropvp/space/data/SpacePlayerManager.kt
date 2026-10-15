package net.astropvp.space.data

import net.astropvp.space.SpacePlugin
import net.astropvp.storage.StoragePlugin
import net.astropvp.storage.gson.Gson
import net.astropvp.storage.utils.SaveLoadManager
import java.util.*

class SpacePlayerManager(spacePlugin: SpacePlugin, gson: Gson) : SaveLoadManager<SpacePlayer>(
    spacePlugin,
    gson,
    "/data",
    SpacePlayer::class.java) {

    init {
        if (StoragePlugin.getInstance() == null) {
            throw NullPointerException("StoragePlugin.getInstance() is null")
        }
    }

    fun getSpacePlayer(uuid: UUID): SpacePlayer {
        if (!contains(uuid.toString())) {
            val spacePlayer = SpacePlayer(uuid)
            addToCache(spacePlayer)
        }
        return getFromCache(uuid.toString())
    }
}