package net.astropvp.space.data.temp

import com.cosmicpvp.cosmicutils.utils.CC
import com.cosmicpvp.cosmicutils.utils.TimeUtils
import net.astropvp.CollectPlugin
import net.astropvp.space.SpacePlugin
import net.astropvp.space.utils.PlayerInventoryContents
import net.astropvp.space.utils.msg
import net.astropvp.struct.CollectCategory
import net.astropvp.struct.CollectType
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import java.util.UUID

class ActiveSpaceData(val uuid: UUID) {
    
    fun player(): Player {
        return Bukkit.getPlayer(uuid)
    }
    

    var grindedItems = mutableListOf<ItemStack>()

    var kills: Int = 0
    var start: Long = System.currentTimeMillis()
    var money: Double = 0.0
    var xp: Int = 0

    fun incrementKills() {
        kills++
    }

    fun addMoney(m: Double) {
        this.money += m
    }

    fun addExp(exp: Int) {
        this.xp += exp
    }

    private lateinit var inventory: PlayerInventoryContents

    fun timeToNiceString(): String {
        return TimeUtils.formatSeconds((System.currentTimeMillis() - start) / 1000)
    }

    fun exit() {
        val player = player()
        clear(player)
        //player.maxHealth = 20.0
        for (activePotionEffect in player.activePotionEffects) {
            player.removePotionEffect(activePotionEffect.type)
        }
        Bukkit.getScheduler().runTaskLater(SpacePlugin.instance, {

            player.msg("&aYou have been safely teleported to spawn and your inventory has been rolled back!")
            clear(player)
            //inventory.restore(player())
            val collect = CollectPlugin.getInstance().collectManager.getOrCreate(player.uniqueId.toString())
            val collectCategory = CollectCategory(CollectType.SPACE, grindedItems.sortedBy { it.type.name }.toTypedArray().clone(), CC.translate("&d&lSpace Loot"), System.currentTimeMillis())
            collect.add(collectCategory)
            grindedItems.clear()
            player.msg("&d&l(!) &dYour &fSpace Loot &dhas been added to a &f/collect &dcategory")
            //player.maxHealth = 20.0
            SpacePlugin.activeSpaceDataManager.remove(player())
        }, 1L)
    }

    fun enter() {
        val player = player()
        player.allowFlight = false
        player.isFlying = false
        player.msg("&dYou have entered the &fSpace Lands&d, be careful...")
        for (activePotionEffect in player.activePotionEffects) {
            player.removePotionEffect(activePotionEffect.type)
        }
        val inv = player.inventory
        //addContentsToPlayerInventoryContents(inv)
        clear(player())
        giveYourItems(inv)

        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 3))
        Bukkit.getScheduler().runTaskLater(SpacePlugin.instance, {
            //player.maxHealth = 32.0
            player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 3))
        }, 5L)

    }

    private fun giveYourItems(inv: PlayerInventory) {
        val player = player()
        val spacePlayer = SpacePlugin.instance.spacePlayerManager!!.getSpacePlayer(player.uniqueId)

        inv.armorContents = arrayOf(spacePlayer.getBoots(), spacePlayer.getLeggings(), spacePlayer.getChestplate(), spacePlayer.getHelmet())
        inv.addItem(spacePlayer.getSword())
        inv.addItem(ItemStack(Material.GOLDEN_APPLE, 64))
        val splash = Potion(PotionType.INSTANT_HEAL, 2)
        splash.isSplash = true
        for (i in 0 until 34) {
            inv.addItem(splash.toItemStack(1))
        }

    }

    private fun addContentsToPlayerInventoryContents(inv: PlayerInventory) {
        inventory = PlayerInventoryContents(inv.contents.clone(), inv.armorContents.clone())
    }

    private fun clear(player: Player) {
        player.inventory.clear()
        player.inventory.armorContents = emptyArray()
        player.inventory.armorContents = null
        player.inventory.helmet = null
        player.inventory.chestplate = null
        player.inventory.leggings = null
        player.inventory.boots = null
        player.removePotionEffect(PotionEffectType.SPEED)
        Bukkit.getScheduler().runTaskLater(SpacePlugin.instance, {
            player.removePotionEffect(PotionEffectType.SPEED)
        }, 1L)
    }

    fun teleportAndExit() {
        val player = player()
        if (CombatLog.inCombat(player)) {
            CombatLog.unflagCombat(player)
        }
        player.teleport(Bukkit.getWorld("world").spawnLocation)
        exit()
    }

}