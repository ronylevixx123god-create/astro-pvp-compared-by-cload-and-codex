package net.astropvp.space

import de.tr7zw.nbtapi.NBTItem
import net.astropvp.space.struct.SpaceAugment
import net.astropvp.space.struct.SpaceAugmentRegister
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object SpaceAPI {

    val WORLDS = listOf("mars", "space")

    fun isAugment(itemStack: ItemStack?): Boolean {
        if(itemStack == null || itemStack.type == Material.AIR) return false
        val nbtItem = NBTItem(itemStack)
        return nbtItem.hasKey("augmentType")
    }

    fun getAugment(itemStack: ItemStack?): SpaceAugment? {
        if(!isAugment(itemStack)) return null
        val name = NBTItem(itemStack).getString("augmentType")
        return SpaceAugmentRegister.augments[name]
    }

    fun getAugmentLevel(itemStack: ItemStack?): Int {
        if(!isAugment(itemStack)) return 0
        return NBTItem(itemStack).getInteger("augmentLevel")
    }

    fun getAugmentLevel(player: Player, augment: String): Int {
        val spacePlayer = SpacePlugin.instance.spacePlayerManager?.getSpacePlayer(player.uniqueId) ?: return 0
        val equipment = listOf(spacePlayer.sword, spacePlayer.helmet, spacePlayer.chestplate, spacePlayer.leggings, spacePlayer.boots)
        return equipment.firstOrNull { it.enchant?.name == augment }?.enchant?.level ?: 0
    }


    fun hasAugmentEquipped(player: Player, augment: String): Boolean {
        val spacePlayer = SpacePlugin.instance.spacePlayerManager?.getSpacePlayer(player.uniqueId) ?: return false
        val equipment = listOf(spacePlayer.sword, spacePlayer.helmet, spacePlayer.chestplate, spacePlayer.leggings, spacePlayer.boots)
        return equipment.any { it.enchant?.name == augment }
    }
}