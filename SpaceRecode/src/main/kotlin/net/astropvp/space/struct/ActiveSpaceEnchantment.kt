package net.astropvp.space.struct

import org.bukkit.inventory.ItemStack

data class ActiveSpaceEnchantment(val name: String, val level: Int) {
    fun build(): ItemStack? {
        return SpaceAugmentRegister.augments[name]?.build(level)
    }
}