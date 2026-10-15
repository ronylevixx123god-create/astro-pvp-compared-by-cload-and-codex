package net.astropvp.space.items

import de.tr7zw.nbtapi.NBTItem
import net.astropvp.space.utils.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ZombieItemPlacer {

    fun getItem(): ItemStack {
        val ib = ItemBuilder(Material.CHEST)
            .setName("Zombie Spawn Location Setter")
            .addLoreLines(listOf(
                "&7Place to set a location for a space zombie to spawn"
            ))
        val item = ib.colorize().build()
        val nbtItem = NBTItem(item)
        nbtItem.setString("savCustomItem", "spaceZombieSpawner")
        return nbtItem.item
    }

}