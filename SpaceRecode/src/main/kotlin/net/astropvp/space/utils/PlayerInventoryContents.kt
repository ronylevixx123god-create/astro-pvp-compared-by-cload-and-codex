package net.astropvp.space.utils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerInventoryContents(var inventoryContents: Array<ItemStack>, var armorContents: Array<ItemStack>) {

    private fun restoreInventory(player: Player) {
        player.inventory.contents = inventoryContents
    }

    private fun restoreArmor(player: Player) {
        player.inventory.armorContents = armorContents
    }

    fun restore(player: Player) {
        restoreInventory(player)
        restoreArmor(player)
        player.updateInventory()
    }

}
