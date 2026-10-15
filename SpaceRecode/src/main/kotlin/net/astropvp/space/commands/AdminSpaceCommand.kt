package net.astropvp.space.commands

import de.tr7zw.nbtapi.NBTItem
import me.mattstudios.mf.annotations.*
import me.mattstudios.mf.base.CommandBase
import net.astropvp.space.SpaceAPI
import net.astropvp.space.hostile.MobManager
import net.astropvp.space.items.ZombieItemPlacer
import net.astropvp.space.struct.SpaceAugmentRegister
import net.astropvp.space.struct.SpaceAugment
import net.astropvp.space.utils.ItemBuilder
import net.astropvp.space.utils.msg
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Command("aspace")
class AdminSpaceCommand : CommandBase() {

    @Default
    @Permission("space.admin")
    fun defaultCommand(player: Player) {

    }

    @SubCommand("killall")
    @Permission("space.admin")
    fun killAllCommand(player: Player) {
        MobManager.activeEntities.forEach { it.value.second.remove() }
        MobManager.activeEntities.clear()
        SpaceAPI.WORLDS.forEach { Bukkit.getWorld(it).entities.filter { entity -> entity !is Player }.forEach { entity -> entity.remove() }}
        SpaceAPI.WORLDS.forEach { Bukkit.getWorld(it).players.forEach { player -> player.msg("&d&l(!) &dAll space mobs have been cleared, new ones will spawn soon")}}
    }

    @SubCommand("zombieplacer")
    @Permission("space.admin")
    fun zombiePlacerCommand(player: Player) {
        player.inventory.addItem(ZombieItemPlacer.getItem())
    }

    @SubCommand("augment")
    @Permission("space.admin")
    @WrongUsage("/aspace augment <receiver> <type> <level>")
    fun spaceAugmentCommand(player: Player, @Completion("#players") receiver: Player, name: String, amount: String) {
        val level = amount.toInt()
        val spaceAugment: SpaceAugment = SpaceAugmentRegister.getSpaceEnchantFromName(name) ?: return
        receiver.inventory.addItem(spaceAugment.build(level))
    }

    @SubCommand("augmentbooster")
    @Permission("space.admin")
    @WrongUsage("/aspace augmentbooster <receiver>")
    fun spaceAugmentBoosterCommand(player: Player, @Completion("#players") receiver: Player, successRate: String) {
        receiver.inventory.addItem(spaceAugmentBooster(successRate.toInt()))
    }

    private fun spaceAugmentBooster(success: Int): ItemStack {
        val ib = ItemBuilder(Material.CLAY)
        ib.setName("&3&lAugment Booster (&f+1 Level&3&l)")
        ib.setLore(listOf(
            "&7Drag n Drop this Augment Booster onto",
            "&7any Space Augment to increase it's level",
            "&7by &f+1 Level &7up to it's level cap",
            "",
            "&3Information",
            "&3┃ &fSuccess Rate: &3${success}%",
            "&3┃ &fDestroy Chance: &30%",
            "",
            "&3&l(!) &3Drag n drop to apply!"
            ))
        val nbtItem = NBTItem(ib.colorize().build())
        nbtItem.setString("astroCustomItem", "spaceAugmentBooster")
        nbtItem.setInteger("successRate", success)
        nbtItem.setInteger("boostAmount", 1)
        return nbtItem.item
    }

}