package net.astropvp.space.inventory

import com.cosmicpvp.cosmicutils.utils.ItemUtils
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import me.astropvp.astropvpskins.AstroItemSkinsAPI
import me.astropvp.astropvpskins.enums.ItemSkinType
import me.astropvp.astropvpskins.enums.SkinType
import net.astropvp.space.data.SpacePlayer
import net.astropvp.space.utils.ItemBuilder
import net.astropvp.space.utils.capitalCase
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SpaceSkinsInventory(val spacePlayer: SpacePlayer) {

    val player: Player = Bukkit.getPlayer(spacePlayer.playerUUID)

    private val gearInventory: Gui = Gui.gui(GuiType.CHEST).rows(4).title(Component.text("Space Skins")).create()

    init {

        updateEntireInventory()
        setupBackButton()
        gearInventory.setDefaultTopClickAction { it.isCancelled = true }

        gearInventory.setDefaultClickAction {
            it.isCancelled = true
            val clickedItem = it.currentItem
            if(AstroItemSkinsAPI.isItemSkin(clickedItem).not()) return@setDefaultClickAction
            val itemSkin = AstroItemSkinsAPI.getItemSkin(clickedItem)[0]
            val applicableItemSkinTypes = listOf(ItemSkinType.BOOTS, ItemSkinType.BELT, ItemSkinType.AMULET, ItemSkinType.HELMET, ItemSkinType.SWORD)
            val type = itemSkin.itemSkinType
            if(applicableItemSkinTypes.contains(type).not()) return@setDefaultClickAction

            it.currentItem = ItemUtils.decreaseItemStackAmountByOne(it.currentItem)

            when (type.name.lowercase()) {
                "helmet" -> { spacePlayer.helmet.skinType = itemSkin }
                "sword" -> { spacePlayer.sword.skinType = itemSkin }
                "amulet" -> { spacePlayer.chestplate.skinType = itemSkin }
                "belt" -> { spacePlayer.leggings.skinType = itemSkin }
                "boots" -> { spacePlayer.boots.skinType = itemSkin }
            }
            updateEntireInventory()
        }

        gearInventory.filler.fill(GuiItem(ItemBuilder(Material.STAINED_GLASS_PANE, 1, 7.toShort()).setName("&7").colorize().build()))
    }

    private fun setupBackButton() {
        val ib = ItemBuilder(Material.BARRIER)
            .setName("&cGo Back")
        gearInventory.setItem(4, 1, GuiItem(ib.colorize().build()) {
            val player = it.whoClicked as Player
            player.closeInventory()
            //player.chat("/space")
            val spaceInventory = SpaceGearInventory(spacePlayer)
            spaceInventory.open()
        })
    }

    private fun updateEntireInventory() {
        addGearItemsToInventory()
        updateInventorySlots()
        gearInventory.update()
    }


    private fun updateInventorySlots() {

        val sword: ItemStack = if(spacePlayer.sword.skinType == null) {
            buildItemSkinOpen(Material.DIAMOND_SWORD)
        } else {
            buildItemSkinSlotInUse(Material.DIAMOND_SWORD, spacePlayer.sword.skinType!!)
        }

        val helmet: ItemStack = if(spacePlayer.helmet.skinType == null) {
            buildItemSkinOpen(Material.DIAMOND_HELMET)
        } else {
            buildItemSkinSlotInUse(Material.DIAMOND_HELMET, spacePlayer.helmet.skinType!!)
        }

        val chestplate: ItemStack = if(spacePlayer.chestplate.skinType == null) {
            buildItemSkinOpen(Material.DIAMOND_CHESTPLATE)
        } else {
            buildItemSkinSlotInUse(Material.DIAMOND_CHESTPLATE, spacePlayer.chestplate.skinType!!)
        }

        val leggings: ItemStack = if(spacePlayer.leggings.skinType == null) {
            buildItemSkinOpen(Material.DIAMOND_LEGGINGS)
        } else {
            buildItemSkinSlotInUse(Material.DIAMOND_LEGGINGS, spacePlayer.leggings.skinType!!)
        }

        val boots: ItemStack = if(spacePlayer.boots.skinType == null) {
            buildItemSkinOpen(Material.DIAMOND_BOOTS)
        } else {
            buildItemSkinSlotInUse(Material.DIAMOND_BOOTS, spacePlayer.boots.skinType!!)
        }



        gearInventory.updateItem(3, 3, GuiItem(sword) {
            if(it.isRightClick) {
                if(spacePlayer.sword.skinType != null) {
                    player.inventory.addItem(spacePlayer.sword.skinType!!.item)
                    spacePlayer.sword.skinType = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 4, GuiItem(helmet) {
            if(it.isRightClick) {
                if(spacePlayer.helmet.skinType != null) {
                    player.inventory.addItem(spacePlayer.helmet.skinType!!.item)
                    spacePlayer.helmet.skinType = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 5, GuiItem(chestplate) {
            if(it.isRightClick) {
                if(spacePlayer.chestplate.skinType != null) {
                    player.inventory.addItem(spacePlayer.chestplate.skinType!!.item)
                    spacePlayer.chestplate.skinType = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 6, GuiItem(leggings) {
            if(it.isRightClick) {
                if(spacePlayer.leggings.skinType != null) {
                    player.inventory.addItem(spacePlayer.leggings.skinType!!.item)
                    spacePlayer.leggings.skinType = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 7, GuiItem(boots) {
            if(it.isRightClick) {
                if(spacePlayer.boots.skinType != null) {
                    player.inventory.addItem(spacePlayer.boots.skinType!!.item)
                    spacePlayer.boots.skinType = null
                    updateEntireInventory()
                }
            }
        })


    }

    private fun addGearItemsToInventory() {
        gearInventory.updateItem(2, 4, GuiItem(spacePlayer.getHelmet()))
        gearInventory.updateItem(2, 5, GuiItem(spacePlayer.getChestplate()))
        gearInventory.updateItem(2, 6, GuiItem(spacePlayer.getLeggings()))
        gearInventory.updateItem(2, 7, GuiItem(spacePlayer.getBoots()))
        gearInventory.updateItem(2, 3, GuiItem(spacePlayer.getSword()))
    }



    private fun buildItemSkinSlotInUse(material: Material, skinType: SkinType): ItemStack {
        val type = material.name.split("_")[1].lowercase()
        val ib = ItemBuilder(skinType.item)
        ib.addLoreLines(listOf(
            "",
            "&fThis item skin will be equipped to",
            "&fyour $type when you enter the space lands",
            "",
            "&d&nRight-Click&r &fto retrieve this item skin!"
        ))
        return ib.colorize().build()
    }

    private fun buildItemSkinOpen(material: Material): ItemStack {
        val ib = ItemBuilder(Material.SLIME_BALL)
            .setName("&a&lInsert Item Skin")
            .setLore(listOf(
                "",
                "&7Click a ${material.name.split("_")[1].capitalCase()} item skin in your",
                "&7inventory and it will apply to this slot!"
            ))
        return ib.colorize().build()
    }

    fun open() {
        this.gearInventory.open(player)
    }

}