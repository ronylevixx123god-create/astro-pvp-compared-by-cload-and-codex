package net.astropvp.space.inventory

import com.cosmicpvp.cosmicutils.utils.ItemUtils
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import net.astropvp.space.SpaceAPI
import net.astropvp.space.data.SpacePlayer
import net.astropvp.space.struct.ActiveSpaceEnchantment
import net.astropvp.space.utils.ItemBuilder
import net.astropvp.space.utils.capitalCase
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SpaceAugmentsInventory(val spacePlayer: SpacePlayer) {

    val player: Player = Bukkit.getPlayer(spacePlayer.playerUUID)

    private val gearInventory: Gui = Gui.gui(GuiType.CHEST).rows(4).title(Component.text("Space Augments")).create()

    init {

        updateEntireInventory()
        setupBackButton()
        gearInventory.setDefaultTopClickAction { it.isCancelled = true }

        gearInventory.setDefaultClickAction {
            it.isCancelled = true
            val clickedItem = it.currentItem
            if(SpaceAPI.isAugment(clickedItem).not()) return@setDefaultClickAction
            val augment = SpaceAPI.getAugment(clickedItem) ?: return@setDefaultClickAction
            val type = augment.appliesTo
            it.currentItem = ItemUtils.decreaseItemStackAmountByOne(it.currentItem)

            when (type.lowercase()) {
                "helmet" -> {
                    if(spacePlayer.helmet.enchant != null) {
                        player.inventory.addItem(spacePlayer.helmet.enchant?.build())
                    }
                    spacePlayer.helmet.enchant = ActiveSpaceEnchantment(augment.name, SpaceAPI.getAugmentLevel(clickedItem))
                }
                "sword" -> {
                    if(spacePlayer.sword.enchant != null) {
                        player.inventory.addItem(spacePlayer.sword.enchant?.build())
                    }
                    spacePlayer.sword.enchant = ActiveSpaceEnchantment(augment.name, SpaceAPI.getAugmentLevel(clickedItem))
                }
                "chestplate" -> {
                    if(spacePlayer.chestplate.enchant != null) {
                        player.inventory.addItem(spacePlayer.chestplate.enchant?.build())
                    }
                    spacePlayer.chestplate.enchant = ActiveSpaceEnchantment(augment.name, SpaceAPI.getAugmentLevel(clickedItem))
                }
                "leggings" -> {
                    if(spacePlayer.leggings.enchant != null) {
                        player.inventory.addItem(spacePlayer.leggings.enchant?.build())
                    }
                    spacePlayer.leggings.enchant = ActiveSpaceEnchantment(augment.name, SpaceAPI.getAugmentLevel(clickedItem))
                }
                "boots" -> {
                    if(spacePlayer.boots.enchant != null) {
                        player.inventory.addItem(spacePlayer.boots.enchant?.build())
                    }
                    spacePlayer.boots.enchant = ActiveSpaceEnchantment(augment.name, SpaceAPI.getAugmentLevel(clickedItem))
                }
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

        val sword: ItemStack = if(spacePlayer.sword.enchant == null) {
            buildAugmentSlotOpen(Material.DIAMOND_SWORD)
        } else {
            buildAugmentSlotInUse(Material.DIAMOND_SWORD, spacePlayer.sword.enchant!!)
        }

        val helmet: ItemStack = if(spacePlayer.helmet.enchant == null) {
            buildAugmentSlotOpen(Material.DIAMOND_HELMET)
        } else {
            buildAugmentSlotInUse(Material.DIAMOND_HELMET, spacePlayer.helmet.enchant!!)
        }

        val chestplate: ItemStack = if(spacePlayer.chestplate.enchant == null) {
            buildAugmentSlotOpen(Material.DIAMOND_CHESTPLATE)
        } else {
            buildAugmentSlotInUse(Material.DIAMOND_CHESTPLATE, spacePlayer.chestplate.enchant!!)
        }

        val leggings: ItemStack = if(spacePlayer.leggings.enchant == null) {
            buildAugmentSlotOpen(Material.DIAMOND_LEGGINGS)
        } else {
            buildAugmentSlotInUse(Material.DIAMOND_LEGGINGS, spacePlayer.leggings.enchant!!)
        }

        val boots: ItemStack = if(spacePlayer.boots.enchant == null) {
            buildAugmentSlotOpen(Material.DIAMOND_BOOTS)
        } else {
            buildAugmentSlotInUse(Material.DIAMOND_BOOTS, spacePlayer.boots.enchant!!)
        }



        gearInventory.updateItem(3, 3, GuiItem(sword) {
            if(it.isRightClick) {
                if(spacePlayer.sword.enchant != null) {
                    player.inventory.addItem(spacePlayer.sword.enchant?.build())
                    spacePlayer.sword.enchant = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 4, GuiItem(helmet) {
            if(it.isRightClick) {
                if(spacePlayer.helmet.enchant != null) {
                    player.inventory.addItem(spacePlayer.helmet.enchant?.build())
                    spacePlayer.helmet.enchant = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 5, GuiItem(chestplate) {
            if(it.isRightClick) {
                if(spacePlayer.chestplate.enchant != null) {
                    player.inventory.addItem(spacePlayer.chestplate.enchant?.build())
                    spacePlayer.chestplate.enchant = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 6, GuiItem(leggings) {
            if(it.isRightClick) {
                if(spacePlayer.leggings.enchant != null) {
                    player.inventory.addItem(spacePlayer.leggings.enchant?.build())
                    spacePlayer.leggings.enchant = null
                    updateEntireInventory()
                }
            }
        })

        gearInventory.updateItem(3, 7, GuiItem(boots) {
            if(it.isRightClick) {
                if(spacePlayer.boots.enchant != null) {
                    player.inventory.addItem(spacePlayer.boots.enchant?.build())
                    spacePlayer.boots.enchant = null
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



    private fun buildAugmentSlotInUse(material: Material, spaceAugment: ActiveSpaceEnchantment): ItemStack {
        val type = material.name.split("_")[1].lowercase()
        val ib = ItemBuilder(Material.INK_SACK, 1, 6)
        ib.setName("&3&lSpace Augment (&f\"${spaceAugment.name.capitalCase()} &8➥ &f${spaceAugment.level}&3&l)")
        ib.addLoreLines(listOf(
            "",
            "&fYour ${spaceAugment.name.capitalCase()} augment will be equipped to",
            "&fyour $type when you enter the space lands",
            "",
            "&d&nRight-Click&r &fto retrieve this augment"
        ))
        return ib.colorize().build()
    }

    private fun buildAugmentSlotOpen(material: Material): ItemStack {
        val ib = ItemBuilder(Material.SLIME_BALL)
            .setName("&3&lInsert Space Augment")
            .setLore(listOf(
                "",
                "&7Click a ${material.name.split("_")[1].capitalCase()} augment in your",
                "&7inventory and it will apply to this slot!"
            ))
        return ib.colorize().build()
    }

    fun open() {
        this.gearInventory.open(player)
    }

}