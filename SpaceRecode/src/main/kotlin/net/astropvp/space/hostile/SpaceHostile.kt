package net.astropvp.space.hostile

import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack

abstract class SpaceHostile {
    abstract fun spawn(location: Location): LivingEntity
    abstract fun getPossibleRewards(): List<ItemStack>
    abstract val amountOfRewards: Int
    abstract fun onDeath(event: EntityDeathEvent, killer: Player)
    abstract fun onPlayerDamageMob(event: EntityDamageByEntityEvent, attacker: Player)
    abstract fun onGetDamagedByMob(event: EntityDamageByEntityEvent, victim: Player)
}
