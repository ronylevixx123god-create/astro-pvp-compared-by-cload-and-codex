package net.astropvp.space.hostile

import net.astropvp.space.hostile.mobs.SpaceZombie
import net.astropvp.space.hostile.mobs.SpaceZombieKing

/**
 * enum wasn't loading on
 */
object MobTypeRegister {

    val mobTypes: MutableMap<String, SpaceHostile> = mutableMapOf()

    fun addMobType(type: String, hostile: SpaceHostile) {
        mobTypes[type] = hostile
    }

    fun get(type: String): SpaceHostile? {
        return mobTypes[type]
    }

    fun getSpaceZombie(): SpaceZombie {
        return mobTypes["SPACE_ZOMBIE"] as SpaceZombie
    }

    fun getSpaceZombieKing(): SpaceZombieKing {
        return mobTypes["SPACE_ZOMBIE_KING"] as SpaceZombieKing
    }

    fun initialize() {
        addMobType("SPACE_ZOMBIE", SpaceZombie())
        addMobType("SPACE_ZOMBIE_KING", SpaceZombieKing())
    }

}