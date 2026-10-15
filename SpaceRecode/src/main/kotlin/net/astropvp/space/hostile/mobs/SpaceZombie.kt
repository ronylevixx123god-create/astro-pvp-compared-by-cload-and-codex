package net.astropvp.space.hostile.mobs

import com.cosmicpvp.cosmicutils.utils.CC
import com.cosmicpvp.cosmicutils.utils.ItemBuilder
import net.astropvp.loottable.LootTableAPI
import net.astropvp.loottable.struct.LootTable
import net.astropvp.space.hostile.MobManager
import net.astropvp.space.hostile.MobType
import net.astropvp.space.hostile.SpaceHostile
import net.astropvp.space.utils.ActionBarMessageUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SpaceZombie : SpaceHostile() {

    override fun spawn(location: Location): LivingEntity {

        val entity = location.world.spawnEntity(location, EntityType.ZOMBIE) as Zombie
        entity.customName = CC.GreenB + "Space Zombie"
        entity.removeWhenFarAway = false
        entity.isCustomNameVisible = true
        entity.canPickupItems = false

        entity.equipment.helmet =
            ItemBuilder(ItemStack(Material.GLASS, 1, 95.toShort()))
                .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .enchant(Enchantment.DURABILITY, 3).build()
        entity.maxHealth = 12.5
        entity.health = entity.maxHealth
        entity.equipment.chestplate = ItemBuilder(Material.IRON_CHESTPLATE)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .enchant(Enchantment.DURABILITY, 3).build()
        entity.equipment.leggings = ItemBuilder(Material.IRON_LEGGINGS)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .enchant(Enchantment.DURABILITY, 3).build()
        entity.equipment.boots = ItemBuilder(Material.DIAMOND_BOOTS)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .enchant(Enchantment.DURABILITY, 3).build()
        entity.equipment.itemInHand = ItemBuilder(Material.DIAMOND_SWORD)
            .enchant(Enchantment.DAMAGE_ALL, 5)
            .enchant(Enchantment.DURABILITY, 3).build()
        entity.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 9999, 2))
        entity.addPotionEffect(PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999, 4))
        if (entity.equipment != null) {
            entity.equipment.itemInHandDropChance = 0.0f
            entity.equipment.helmetDropChance = 0.0f
            entity.equipment.chestplateDropChance = 0.0f
            entity.equipment.leggingsDropChance = 0.0f
            entity.equipment.bootsDropChance = 0.0f
        }

        MobManager.add("SPACE_ZOMBIE", entity)
        return entity
    }

    override val amountOfRewards: Int
        get() = 3

    override fun getPossibleRewards(): List<ItemStack> {
        return LootTableAPI.getAllLootFromLootTable(LootTable.SPACE)
    }

    override fun onDeath(event: EntityDeathEvent, killer: Player) {
    }

    override fun onPlayerDamageMob(event: EntityDamageByEntityEvent, attacker: Player) {
        ActionBarMessageUtils.sendMessage(attacker,
            CC.translate("&a&lSpace Zombie &7(&f" + Math.round((event.entity as LivingEntity).health) + "/" + Math.round((event.entity as LivingEntity).maxHealth) + " HP&7) ")
        )
    }

    override fun onGetDamagedByMob(event: EntityDamageByEntityEvent, victim: Player) {
    }
}
