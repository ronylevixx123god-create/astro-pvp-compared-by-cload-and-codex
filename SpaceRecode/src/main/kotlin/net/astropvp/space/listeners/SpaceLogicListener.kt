package net.astropvp.space.listeners

import com.cosmicpvp.cosmicutils.utils.CC
import me.astropvp.astropvpskins.AstroItemSkinsAPI
import me.astropvp.astropvpskins.enums.SkinType
import net.astropvp.loottable.LootTableAPI
import net.astropvp.loottable.struct.LootTable
import net.astropvp.space.SpaceAPI
import net.astropvp.space.SpacePlugin
import net.astropvp.space.hostile.MobManager
import net.astropvp.space.hostile.MobTypeRegister
import net.astropvp.space.utils.msg
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.random.Random

class SpaceLogicListener : Listener {

    @EventHandler
    fun onSpaceMobDeath(event: EntityDeathEvent) {
        if(event.entity is Player) {
            val spacePlayer = SpacePlugin.activeSpaceDataManager.get(event.entity as Player) ?: return
            spacePlayer.teleportAndExit()
            return
        }
        if(MobManager.isActiveEntity(event.entity).not()) return
        val player = event.entity.killer ?: return
        val spacePlayer = SpacePlugin.activeSpaceDataManager.get(player) ?: return
        val mobType = MobManager.activeEntities[event.entity.entityId]?.first ?: return
        val hostile = MobTypeRegister.get(mobType) ?: return
        hostile.onDeath(event, player)
        //MobType.getInstance(mobType).onDeath(event, player)
        event.drops.clear()
        event.droppedExp = 0
        MobManager.remove(event.entity)
        val rewardsToPlayer: MutableList<ItemStack> = mutableListOf()
        var chance = 0.5

        if(event.entity.world.name == "space") {
            chance *= 0.2
        }
        if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.SUMMER_2021_BELT)) {
            chance *= 1.30
        }
        for (i in 0..hostile.amountOfRewards) {
            if (Math.random() < chance) {
                val reward = LootTableAPI.getRandomLootItem(LootTable.SPACE)
                rewardsToPlayer.add(reward)
                player.msg("&aYou have received a ${reward.itemMeta.displayName} &afrom the &dSpace Lands&a, it will be in your &d/collect.")

                if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.NYE_AMULET) && Math.random() < 0.15) {
                    player.sendMessage(CC.translate("&d&l(Radioactive Amulet) &dYou were &flucky&d and received &fdouble&d space loot."))
                    rewardsToPlayer.add(LootTableAPI.getRandomLootItem(LootTable.SPACE))
                }
            }
        }

        for (itemStack in rewardsToPlayer) {
            spacePlayer.grindedItems.add(itemStack)
        }
        //spacePlayer.grindedItems.addAll(rewardsToPlayer)
        //rewardsToPlayer.forEach { player.inventory.addItem(it) }

    }

    @EventHandler
    fun onSpaceMobDamage(event: EntityDamageByEntityEvent) {
        if(!SpaceAPI.WORLDS.contains(event.damager.world.name)) return
        if(event.entity is Player) {
            if(event.damager !is Player) {
                if(SpaceAPI.hasAugmentEquipped(event.entity as Player, "reinforced")) {
                    if(Random.nextDouble(100.0) < 0.5*SpaceAPI.getAugmentLevel(event.entity as Player, "reinforced")) {
                        val reducedDmgPerLevel = 0.04
                        val reducedDmg = reducedDmgPerLevel * SpaceAPI.getAugmentLevel(event.entity as Player, "reinforced")
                        event.damage *= (1-reducedDmg)
                        event.damager.sendMessage(CC.translate("&3&lSpace Augment &8➥ &7Your &6Reinforced &7augment has taken effect!"))
                    }
                }
                event.damage *= 1+(((200..600).random())/100)
                if(MobManager.isActiveEntity(event.damager.entityId).not()) return
                val mobType = MobManager.activeEntities[event.damager.entityId]?.first ?: return
                val hostile = MobTypeRegister.get(mobType) ?: return

                hostile.onGetDamagedByMob(event, event.entity as Player)
            } else {
                event.damage *= 1+(((100..300).random())/100)

            }



            if(SpaceAPI.hasAugmentEquipped(event.entity as Player, "retaliate")) {
                if(Random.nextDouble(100.0) < 0.1*SpaceAPI.getAugmentLevel(event.entity as Player, "retaliate")) {
                    val dmgPerLevel = 0.5
                    val dmg = dmgPerLevel * SpaceAPI.getAugmentLevel(event.entity as Player, "retaliate")
                    if(event.damager is Player) {
                        (event.damager as Player).damage(dmg)
                    } else {
                        (event.damager as LivingEntity).damage(dmg * 4)
                    }
                    event.entity.sendMessage(CC.translate("&3&lSpace Augment &8➥ &7Your &aRetaliate &7augment has taken effect!"))
                }
            }

            return
        }

        if(event.damager is Player) {
            if(event.entity is Player) return
            if(MobManager.isActiveEntity(event.entity.entityId).not()) return
            val mobType = MobManager.activeEntities[event.damager.entityId]?.first ?: return
            val hostile = MobTypeRegister.get(mobType) ?: return
            if(SpaceAPI.hasAugmentEquipped(event.damager as Player, "terminate")) {
                if(Random.nextDouble(100.0) < 0.1*SpaceAPI.getAugmentLevel(event.damager as Player, "terminate")) {
                    event.damage *= 1000
                    event.damager.sendMessage(CC.translate("&3&lSpace Augment &8➥ &7Your &4Terminate &7augment has taken effect!"))
                }
            }
            if(SpaceAPI.hasAugmentEquipped(event.damager as Player, "sweeping_edge")) {
                if(Random.nextDouble(100.0) < 0.1*SpaceAPI.getAugmentLevel(event.damager as Player, "sweeping_edge")) {
                    event.entity.getNearbyEntities(7.0, 7.0, 7.0)
                        .filter { !it.isDead }
                        .filter { it !is Player }
                        .filterIsInstance<LivingEntity>()
                        .forEach {
                            it.damage(4.0)
                            val direction = it.location.direction.normalize()
                            it.velocity = direction.multiply(-1)
                        }
                    event.damager.sendMessage(CC.translate("&3&lSpace Augment &8➥ &7Your &bSweeping Edge &7augment has taken effect!"))
                }
            }
            hostile.onPlayerDamageMob(event, event.entity as Player)
            return
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEnterSpaceWorld(event: PlayerChangedWorldEvent) {
        val player = event.player
        val fromWorld = event.from
        val toWorld = player.world

        // If the player changes between two space worlds, cancel the event and warn them
        if (SpaceAPI.WORLDS.contains(toWorld.name) && SpaceAPI.WORLDS.contains(fromWorld.name)) {
            val activeSpacePlayer = SpacePlugin.activeSpaceDataManager.get(player) ?: return
            activeSpacePlayer.exit()
            player.msg("&cWtf did you just do, contact admin please")
            return
        }

        // If the player enters a space world
        if (SpaceAPI.WORLDS.contains(toWorld.name)) {
            val spacePlayer = SpacePlugin.activeSpaceDataManager.getOrCreate(player)
            spacePlayer.enter()
        }

        // If the player exits a space world
        if (SpaceAPI.WORLDS.contains(fromWorld.name)) {
            val activeSpacePlayer = SpacePlugin.activeSpaceDataManager.get(player)
            if(activeSpacePlayer == null) {
                println("[SpaceRecode] Internal error occurred while leaving space: " + player.name)
                return
            }
            activeSpacePlayer.exit()
        }
    }


    fun isInventoryAndArmorEmpty(player: Player): Boolean {
        return player.inventory.contents.all { it == null || it.type == Material.AIR } && player.inventory.armorContents.all { it == null || it.type == Material.AIR }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEnterSpaceWorld(event: PlayerTeleportEvent) {
        val player = event.player
        if (SpaceAPI.WORLDS.contains(event.to.world.name) && !SpaceAPI.WORLDS.contains(event.from.world.name)) {
            if(!isInventoryAndArmorEmpty(player)) {
                if(!player.isOp || !player.hasPermission("space.bypass")) {
                    event.isCancelled = true
                    player.msg("&cYou can only enter the &4Space Lands &cwith an empty inventory!")
                } else {
                    player.msg("&cAs an &4Admin &cyou bypassed the Empty Inventory check for the &4Space Lands!")
                }
            }
        }
        if (SpaceAPI.WORLDS.contains(event.from.world.name) && !SpaceAPI.WORLDS.contains(event.to.world.name)) {
            event.player.inventory.clear()
            event.player.inventory.armorContents = emptyArray()
        }
    }


    /*
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEnterSpaceWorld(event: PlayerTeleportEvent) {
        val to = event.to
        val from = event.from
        if(to.world == from.world) return

        if(event.isCancelled) return

        if(SpaceAPI.WORLDS.contains(to.world.name) && SpaceAPI.WORLDS.contains(from.world.name)) {
            event.isCancelled = true
            val activeSpacePlayer = SpacePlugin.activeSpaceDataManager.get(event.player) ?: return
            activeSpacePlayer.exit()
            event.player.msg("&cWtf did you just do, contact admin please")
            return
        }

        if(SpaceAPI.WORLDS.contains(to.world.name)) {
            val spacePlayer = SpacePlugin.activeSpaceDataManager.getOrCreate(event.player)
            spacePlayer.enter()
        }

        if(SpaceAPI.WORLDS.contains(from.world.name)) {
            val activeSpacePlayer = SpacePlugin.activeSpaceDataManager.get(event.player) ?: return
            activeSpacePlayer.exit()
        }
    }

     */





    var allowedCommands: MutableList<String> = mutableListOf(
        "msg",
        "message",
        "staffmode",
        "staff",
        "freeze",
        "lms",
        "ft",
        "filter",
        "r",
        "bless",
        "blessme",
        "response",
        "space",
        "bless",
        "heal",
        "cf",
        "coinflip",
        "jackpot",
        "feed",
        "f",
        "fix",
        "filter edit",
        "filter toggle",
        "ft",
        "bless",
        "r",
        "reply",
        "spawn",
        "pots",
        "filters",
        "outpost",
        "pin",
        "sc",
        "staffchat",
        "mute",
        "ban",
        "tempban",
        "pin",
        "space refill"
    )

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        if (!SpaceAPI.WORLDS.contains(event.player.world.name)) {
            return
        }

        if (player.isOp) {
            return
        }

        if (player.hasPermission("astrocore.staffmode")) {
            return
        }
        if (allowedCommands.contains(
                event.message.split(" ".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].replace("/", "").lowercase(Locale.getDefault()))) {
            return
        }
        player.sendMessage(CC.translate("&c&l(!) &c" + event.message + " is denied in this world!"))
        event.message = "/null"
        event.isCancelled = true
    }


}