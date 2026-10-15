package net.astropvp.space.inventory

import com.cosmicpvp.cosmicutils.utils.CC
import com.cosmicpvp.cosmicutils.utils.InventoryUtils
import com.cosmicpvp.cosmicutils.utils.ItemUtils
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import me.astropvp.astropvpskins.AstroItemSkinsAPI
import me.astropvp.astropvpskins.enums.ItemSkinType
import me.astropvp.astropvpskins.enums.SkinType
import net.astropvp.space.data.SpacePlayer
import net.astropvp.space.struct.SpaceAugmentRegister
import net.astropvp.space.utils.ItemBuilder
import net.astropvp.space.utils.capitalCase
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SpaceGearInventory(val spacePlayer: SpacePlayer) {

    val player: Player = Bukkit.getPlayer(spacePlayer.playerUUID)

    private val gearInventory: Gui = Gui.gui(GuiType.CHEST).rows(3).title(Component.text("Space Gear")).create()

    init {
        gearInventory.setDefaultTopClickAction { it.isCancelled = true }
        gearInventory.setItem(2, 4, GuiItem(getSkinsItem()) {
            val player = it.whoClicked as Player
            player.closeInventory()
            val spaceSkinsInventory = SpaceSkinsInventory(spacePlayer)
            spaceSkinsInventory.open()
        })

        gearInventory.setItem(2, 6, GuiItem(getAugmentsItem()) {
            val player = it.whoClicked as Player
            player.closeInventory()
            val spaceAugmentsInventory = SpaceAugmentsInventory(spacePlayer)
            spaceAugmentsInventory.open()
        })
        gearInventory.filler.fill(GuiItem(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7.toShort()).setName("&7").colorize().build()))
    }

    private fun getSkinsItem(): ItemStack {
        val ib = ItemBuilder(Material.SLIME_BALL)
        ib.setName("&a&lSpace Skins")
        ib.setLore(listOf(
            "",
            "&7Click to view and edit all the",
            "&7item skins that you will take",
            "&7with you to the &fSpace Lands",
            "",
            "&a&l(!) &aClick to view"
        ))
        return ib.colorize().build()
    }

    private fun getAugmentsItem(): ItemStack {
        val ib = ItemBuilder(Material.INK_SACK, 1, 6)
        ib.setName("&3&lSpace Augments")
        ib.setLore(listOf(
            "",
            "&7Click to view and edit all the",
            "&7augments that you will take",
            "&7with you to the &fSpace Lands",
            "",
            "&3&l(!) &aClick to view"
        ))
        return ib.colorize().build()
    }

    fun open() {
        this.gearInventory.open(player)
    }

}