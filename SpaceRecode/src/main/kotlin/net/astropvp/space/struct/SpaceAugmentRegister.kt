package net.astropvp.space.struct

object SpaceAugmentRegister {

    val augments: HashMap<String, SpaceAugment> = HashMap()

    fun registerAll() {
        val augments = listOf(
            SpaceAugment("terminate", 6, listOf(
                "&3┃ &fChance to terminate a space enemy",
                "&3┃ &fin a single blow."
            ), "sword"),
            SpaceAugment("lucky", 5, listOf(
                "&3┃ &fChance to drop more loot",
                "&3┃ &ffrom space enemies"
            ), "helmet"),
            SpaceAugment("sweeping_edge", 5, listOf(
                "&3┃ &fChance to cause an AoE swipe that",
                "&3┃ &fattacks multiple nearby space enemies"
            ), "sword"),
            SpaceAugment("reinforced", 8, listOf(
                "&3┃ &fReduces damage taken from",
                "&3┃ &fall space enemies "
            ), "chestplate"),
            SpaceAugment("retaliate", 4, listOf(
                "&3┃ &fChance to damage players & space",
                "&3┃ &fenemies when they attack you"
            ), "leggings"),
        )
        augments.forEach { register(it) }
    }

    private fun register(spaceEnchant: SpaceAugment) {
        augments[spaceEnchant.name] = spaceEnchant
    }

    fun getSpaceEnchantFromName(type: String): SpaceAugment? {
        return augments[type]
    }

}