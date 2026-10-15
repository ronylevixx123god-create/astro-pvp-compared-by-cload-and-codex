package net.astropvp.space.commands

import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import net.astropvp.space.SpaceAPI
import net.astropvp.space.SpacePlugin
import net.astropvp.space.inventory.SpaceGearInventory
import net.astropvp.space.inventory.SpaceInventory
import net.astropvp.space.refill.RefillStation
import net.astropvp.space.utils.msg
import org.bukkit.entity.Player

@Command("space")
@Alias("mars", "expeditions", "exped", "expedition")
class SpaceCommand : CommandBase() {

    @Default
    fun defaultCommand(player: Player) {
        val spaceInventory = SpaceInventory(player)
        spaceInventory.open()
        player.msg("&a&l(!) &aOpening the Space Menu for you now..")
    }

    @SubCommand("gear")
    fun gearCommand(player: Player) {
        if(SpaceAPI.WORLDS.contains(player.location.world.name)) {
            player.msg("&c&l(!) &cYou cannot edit your space gear while in the space zone!")
            return
        }
        val spaceGearInventory = SpaceGearInventory(SpacePlugin.instance.spacePlayerManager!!.getSpacePlayer(player.uniqueId))
        spaceGearInventory.open()
    }

    @SubCommand("refill")
    fun refillCommand(player: Player) {
        if(SpaceAPI.WORLDS.contains(player.world.name).not()) return
        RefillStation.tryRefill(player)
    }

}