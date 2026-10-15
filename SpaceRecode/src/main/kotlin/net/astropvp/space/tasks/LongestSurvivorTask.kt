package net.astropvp.space.tasks

import com.cosmicpvp.cosmicutils.utils.CC
import net.astropvp.space.SpacePlugin
import net.astropvp.space.utils.Clickable
import net.astropvp.space.utils.StringUtils
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class LongestSurvivorTask : BukkitRunnable() {

    override fun run() {
        val longestSurvivor = SpacePlugin.activeSpaceDataManager.findLongestSurvivor() ?: return
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendMessage("")
            player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&8«&b✩&8» &b&l" + longestSurvivor.player().name + "'s Space Land Stats" + " &8«&b✩&8»")))
            player.sendMessage(
                StringUtils.getCenteredMessage(
                    CC.translate(
                        "&f* &7Mobs Killed:&b ${longestSurvivor.kills}"
                    )
                )
            )
            player.sendMessage(
                StringUtils.getCenteredMessage(
                    CC.translate(
                        "&f* &7Time Spent:&b " + longestSurvivor.timeToNiceString()
                    )
                )
            )
            player.sendMessage("")
            val spaceClickable = Clickable("")
            spaceClickable.add(
                StringUtils.getCenteredMessage(CC.translate("&7Click &f&nhere&7 to view " + longestSurvivor.player().name + "'s collected rewards.")),
                CC.translate("&7View " + longestSurvivor.player().name + "'s inventory"),
                "/invsee " + longestSurvivor.player().name
            )
            spaceClickable.sendToPlayer(player)
            player.sendMessage("")
        }
    }

}