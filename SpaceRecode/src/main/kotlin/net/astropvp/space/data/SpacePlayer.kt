package net.astropvp.space.data

import com.rit.sucy.EnchantmentAPI
import net.astropvp.space.data.items.*
import net.astropvp.space.utils.ItemBuilder
import net.astropvp.space.utils.capitalCase
import net.astropvp.storage.utils.KeyToSave
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*

data class SpacePlayer(
    var playerUUID: UUID,
    var helmet: SpaceHelmet,
    var chestplate: SpaceChestplate,
    var leggings: SpaceLeggings,
    var boots: SpaceBoots,
    var sword: SpaceSword,
    var multiplier: Double = 1.0,
) : KeyToSave {

    constructor(playerUUID: UUID) : this(playerUUID, SpaceHelmet(), SpaceChestplate(), SpaceLeggings(), SpaceBoots(), SpaceSword(), 1.0)

    fun getSword(): ItemStack {
        val player = Bukkit.getPlayer(playerUUID)
        val ib = ItemBuilder(Material.DIAMOND_SWORD)
            .setName("&d&l${player.name}'s Space Sword")
            .setLore(listOf(
                "",
                "&7Space Sword..."
            ))
        ib.addEnchantment(Enchantment.DAMAGE_ALL, 5)
        ib.addEnchantment(Enchantment.DURABILITY, 3)
        ib.setUnbreakable(true)

        ib.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
        ib.addItemFlag(ItemFlag.HIDE_UNBREAKABLE)

        var itemStack = ib.colorize().build()
        if(sword.skinType != null) {
            itemStack = sword.skinType!!.apply(itemStack)
        }
        if(sword.enchant != null) {
            itemStack = ItemBuilder(itemStack).addLoreLine("&3&l+ AUGMENT [&f${sword.enchant?.name?.capitalCase()} &8➥ &f${sword.enchant?.level}&3&l]").colorize().build()
        }
        return itemStack
    }

    fun getHelmet(): ItemStack {
        var itemStack = buildArmorItem(Material.DIAMOND_HELMET)
        if(helmet.skinType != null) {
            itemStack = helmet.skinType!!.apply(itemStack)
        }
        if(helmet.enchant != null) {
            itemStack = ItemBuilder(itemStack).addLoreLine("&3&l+ AUGMENT [&f${helmet.enchant?.name?.capitalCase()} &8➥ &f${helmet.enchant?.level}&3&l]").colorize().build()
        }
        return itemStack
    }

    fun getChestplate(): ItemStack {
        var itemStack = buildArmorItem(Material.DIAMOND_CHESTPLATE)
        if(chestplate.skinType != null) {
            itemStack = chestplate.skinType!!.apply(itemStack)
        }
        if(chestplate.enchant != null) {
            itemStack = ItemBuilder(itemStack).addLoreLine("&3&l+ AUGMENT [&f${chestplate.enchant?.name?.capitalCase()} &8➥ &f${chestplate.enchant?.level}&3&l]").colorize().build()
        }
        val enchantment = EnchantmentAPI.getEnchantment("overload")
        itemStack = enchantment.addToItem(itemStack, 3)
        return itemStack
    }

    fun getLeggings(): ItemStack {
        var itemStack = buildArmorItem(Material.DIAMOND_LEGGINGS)
        if(leggings.skinType != null) {
            itemStack = leggings.skinType!!.apply(itemStack)
        }
        if(leggings.enchant != null) {
            itemStack = ItemBuilder(itemStack).addLoreLine("&3&l+ AUGMENT [&f${leggings.enchant?.name?.capitalCase()} &8➥ &f${leggings.enchant?.level}&3&l]").colorize().build()
        }
        return itemStack
    }

    fun getBoots(): ItemStack {
        var itemStack = buildArmorItem(Material.DIAMOND_BOOTS)
        if(boots.skinType != null) {
            itemStack = boots.skinType!!.apply(itemStack)
        }
        if(boots.enchant != null) {
            itemStack = ItemBuilder(itemStack).addLoreLine("&3&l+ AUGMENT [&f${boots.enchant?.name?.capitalCase()} &8➥ &f${boots.enchant?.level}&3&l]").colorize().build()
        }
        return itemStack
    }

    private fun buildArmorItem(material: Material): ItemStack {
        val player = Bukkit.getPlayer(playerUUID)
        val type = material.name.split("_")[1]
        val ib = ItemBuilder(material)
            .setName("&d&l${player.name}'s Space ${type.capitalCase()}")
            .setLore(listOf(
                "",
                "&7Space ${type.capitalCase()}..."
            ))
        ib.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
        ib.addEnchantment(Enchantment.DURABILITY, 3)
        ib.setUnbreakable(true)

        ib.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
        ib.addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
        return ib.colorize().build()
    }

    override fun getKey(): String {
        return playerUUID.toString()
    }

    override fun setKey(uuid: String?) {
        playerUUID = UUID.fromString(uuid)
    }

}