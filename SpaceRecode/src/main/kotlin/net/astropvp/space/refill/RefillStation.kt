package net.astropvp.space.refill

import net.astropvp.space.utils.TimeUtils
import net.astropvp.space.utils.msg
import net.astropvp.space.utils.playErrorSound
import net.astropvp.space.utils.playSuccessSound
import org.bukkit.entity.Player
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionType

object RefillStation {

    private val lastRefills: MutableMap<Player, Long> = mutableMapOf()

    private fun canRefill(player: Player): Boolean {
        if (lastRefills.containsKey(player).not()) {
            lastRefills[player] = System.currentTimeMillis()
            return true
        }

        if((lastRefills[player]!! + 30 * 1000L) > (System.currentTimeMillis())) {
            return false
        }
        return true
    }

    fun tryRefill(player: Player) {
        if(!canRefill(player)) {
            player.msg("&cYou cannot refill for another ${TimeUtils.formatMs((System.currentTimeMillis() - lastRefills[player]!!) / 1000L)}")
            player.playErrorSound()
            return
        }
        val splash = Potion(PotionType.INSTANT_HEAL, 2)
        splash.isSplash = true
        for (i in 0 until 36) {
            player.inventory.addItem(splash.toItemStack(1))
        }
        player.playSuccessSound()

        lastRefills[player] = System.currentTimeMillis()
    }
}