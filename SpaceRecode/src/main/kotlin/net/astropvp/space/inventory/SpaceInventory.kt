package net.astropvp.space.inventory

import com.cosmicpvp.cosmicutils.utils.CC
import com.cosmicpvp.cosmicutils.utils.InventoryUtils
import com.cosmicpvp.cosmicutils.utils.ItemBuilder
import dev.triumphteam.gui.components.GuiType
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import net.astropvp.loottable.LootTableAPI
import net.astropvp.loottable.struct.LootTable
import net.astropvp.space.SpacePlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


class SpaceInventory(val player: Player) {

    private val spaceGUI: Gui = Gui.gui(GuiType.CHEST).rows(3).title(Component.text("Space Menu")).create()

    init {

        spaceGUI.setDefaultClickAction { event: InventoryClickEvent -> event.isCancelled = true }

        setupGearButton(2, 7)
        setupWarpMarsButton(2, 6)

        setupWarpSpaceButton(2, 4)
        setupPossibleLootButton(2, 3)

        spaceGUI.filler.fill(
            GuiItem(ItemBuilder(ItemStack(Material.STAINED_GLASS_PANE, 1, 7.toShort())).name(CC.translate("&7")).build())
        )
    }

    private fun setupPossibleLootButton(row: Int, col: Int) {
        val lootItem = ItemBuilder(Material.CHEST, CC.translate("&6&lPossible Loot"))
            .addLoreLine(CC.translate("&7Possible loot you can get from killing"))
            .addLoreLine(CC.translate("&7space creatures in the space lands."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&7&nClick here&7 to view the loot."))
            .build()

        spaceGUI.setItem(row, col, GuiItem(
            lootItem
        ) { event: InventoryClickEvent? ->
            player.openInventory(getLootInventory())
        })
    }

    private fun setupWarpSpaceButton(row: Int, col: Int) {
        val shitWarp = ItemBuilder(Material.ENDER_STONE, CC.translate("&d&lSpace Warp &7(PvP Disabled)"))
            .addLoreLine(CC.translate("&7The space lands have been invaded by numerous"))
            .addLoreLine(CC.translate("&7weird creatures. &bAstronauts&7 team up to try and"))
            .addLoreLine(CC.translate("&7fight against these monsters."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&7The creatures have already invaded certain"))
            .addLoreLine(CC.translate("&7spots in the space lands. Fight the monsters"))
            .addLoreLine(CC.translate("&7to obtain some of the &bbest items&7 in the universe."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&7Once you are in the space lands, you can"))
            .addLoreLine(CC.translate("&7type &b/space stats&7 to view your space stats."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&dHeroic Enchantments&7 &7and &6Holy Whitescrolls&7."))
            .addLoreLine(CC.translate("&7are disabled in the space lands."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&cPvP is &ndisabled&r &cat this warp"))
            .addLoreLine(CC.translate("&cDrop Chances are reduced by 80%"))
            .addLoreLine("")
            .addLoreLine(CC.translate("&7&nClick here&7 to teleport to the space lands."))
            .build()


        spaceGUI.setItem(row, col, GuiItem(
            shitWarp
        ) { event: InventoryClickEvent ->
            event.whoClicked.closeInventory()
            player.chat("/warp space")
        })
    }

    private fun setupWarpMarsButton(row: Int, col: Int) {
        val infoItem = ItemBuilder(Material.NETHERRACK, CC.translate("&4&lMars Warp &c(PvP Enabled)"))
            .addLoreLine(CC.translate("&7The space lands have been invaded by numerous"))
            .addLoreLine(CC.translate("&7weird creatures. &bAstronauts&7 team up to try and"))
            .addLoreLine(CC.translate("&7fight against these monsters."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&7The creatures have already invaded certain"))
            .addLoreLine(CC.translate("&7spots in the space lands. Fight the monsters"))
            .addLoreLine(CC.translate("&7to obtain some of the &bbest items&7 in the universe."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&7Once you are in the space lands, you can"))
            .addLoreLine(CC.translate("&7type &b/space stats&7 to view your space stats."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&dHeroic Enchantments&7 &7and &6Holy Whitescrolls&7."))
            .addLoreLine(CC.translate("&7are disabled in the space lands."))
            .addLoreLine("")
            .addLoreLine(CC.translate("&cThere are currently &4&n" + activePlayersAtMars + "&r &cactive players at /warp mars"))
            .addLoreLine("")
            .addLoreLine(CC.translate("&7&nClick here&7 to teleport to the mars lands."))
            .build()
        spaceGUI.setItem(row, col, GuiItem(
            infoItem
        ) { event: InventoryClickEvent ->
            event.whoClicked.closeInventory()
            player.chat("/warp mars")
        })

    }

    private fun setupGearButton(row: Int, col: Int) {
        val ib = net.astropvp.space.utils.ItemBuilder(Material.DIAMOND_CHESTPLATE)
            .setName("&d&lSpace Gear")
            .setLore(listOf(
                "",
                "&7Click to view and edit the space",
                "&7gear that you will take with you",
                "&7to the /space realm!"
            ))
        this.spaceGUI.setItem(row, col, GuiItem(ib.colorize().build()) {
            val player = it.whoClicked as Player
            player.closeInventory()
            player.chat("/space gear")
            //val spaceGearInventory = SpaceGearInventory(SpacePlugin.instance.spacePlayerManager!!.getSpacePlayer(player.uniqueId))
            //spaceGearInventory.open()
        })

    }


    private fun getLootInventory(): Inventory {
        val items: List<ItemStack> = LootTableAPI.getAllLootFromLootTable(LootTable.SPACE)
        val inventory = Bukkit.createInventory(
            null,
            InventoryUtils.fitSize(items.size),
            CC.DarkGray + "Space Loot Items"
        )
        var pos = 0
        for (item in items) inventory.setItem(pos++, item)

        return inventory
    }

    private val activePlayersAtMars: Int
        get() = Bukkit.getWorld("mars").players.size

    fun open() {
        this.spaceGUI.open(this.player)
    }
}