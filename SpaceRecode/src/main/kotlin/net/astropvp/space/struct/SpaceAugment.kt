package net.astropvp.space.struct

import de.tr7zw.nbtapi.NBTItem
import net.astropvp.space.utils.ItemBuilder
import net.astropvp.space.utils.capitalCase
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class SpaceAugment(val name: String, val maxLevel: Int, private val lore: List<String>, val appliesTo: String) {

    fun build(level: Int): ItemStack {
        val ib = ItemBuilder(Material.INK_SACK, 1, 6)
        ib.setGlowing()
        ib.addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
        ib.addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
        ib.setName("&3&lSpace Augment (&f\"${name.capitalCase()}\"&3&l)")
        ib.setLore(listOf(
            "&7Space Augments are applicable items which",
            "&7make your active space set stronger.",
            "",
            "&3Augment Description"
        ))
        ib.addLoreLines(lore)
        ib.addLoreLines(listOf(
            "",
            "&3Augment Information",
            "&3┃ &fApplies To: &3${appliesTo.capitalCase()}",
            "&3┃ &fLevel: &f${level}&3/${maxLevel}",
            "",
            "&7This &3augment &7can be applied to",
            "&7your space set via the &3/space &7menu"
        ))
        val nbtItem = NBTItem(ib.colorize().build())
        nbtItem.setString("astroCustomItem", "spaceAugment")
        nbtItem.setString("augmentType", name)
        nbtItem.setInteger("augmentLevel", level)
        return nbtItem.item
    }

}