package net.astropvp.space.data.items

import me.astropvp.astropvpskins.enums.SkinType
import net.astropvp.space.struct.ActiveSpaceEnchantment

abstract class SpaceItem {

    abstract var skinType: SkinType?
    abstract var enchant: ActiveSpaceEnchantment?
}