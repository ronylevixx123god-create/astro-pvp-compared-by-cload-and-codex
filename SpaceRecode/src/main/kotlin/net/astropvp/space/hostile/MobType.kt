package net.astropvp.space.hostile

import net.astropvp.space.hostile.mobs.SpaceZombie
import net.astropvp.space.hostile.mobs.SpaceZombieKing

enum class MobType(private val mobClass: Class<out SpaceHostile>, val rewards: Int) {
    //SPACE_ZOMBIE(SpaceZombie::class.java, 3),
    //SPACE_ZOMBIE_KING(SpaceZombieKing::class.java, 7)
    ;

    /*
    companion object {
        private val mobs: MutableMap<MobType, SpaceHostile> = mutableMapOf()

        init {
            entries.forEach { mobType ->
                try {
                    mobs[mobType] = mobType.mobClass.getDeclaredConstructor().newInstance()
                } catch (e: Exception) {
                    println("Error initializing mob type $mobType: ${e.message}")
                    e.printStackTrace()
                }
            }
        }


        fun getInstance(mobType: MobType): SpaceHostile {
            return mobs.getOrPut(mobType) {
                try {
                    mobType.mobClass.getDeclaredConstructor().newInstance()
                } catch (e: Exception) {
                    throw IllegalStateException("Failed to create mob instance for $mobType", e)
                }
            }
        }

    }

     */
}
