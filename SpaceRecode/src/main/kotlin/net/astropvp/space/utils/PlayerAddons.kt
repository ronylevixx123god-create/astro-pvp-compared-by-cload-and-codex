package net.astropvp.space.utils

import com.cosmicpvp.cosmicutils.utils.CC
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun Player.msg(message: String) {
    this.sendMessage(CC.translate(message))
}

fun CommandSender.msg(message: String) {
    this.sendMessage(CC.translate(message))
}

fun Player.playErrorSound() {
    this.playSound(this.location, Sound.ANVIL_LAND, 1.0f, 1.0f)
}

fun Player.playSuccessSound() {
    this.playSound(this.location, Sound.SUCCESSFUL_HIT, 1.0f, 1.0f)
}

fun String.sendToAllOnlinePlayers() {
    for (onlinePlayer in Bukkit.getOnlinePlayers()) {
        onlinePlayer.msg(this)
    }
}

fun String.capitalCase(): String {
    val sep = "_,.-;"
    val sb = StringBuffer()
    var cap = true
    for (i in this.indices) {
        cap = if (sep.indexOf(this[i]) >= 0) {
            sb.append(" ")
            true
        } else {
            if (cap) sb.append(this[i].toUpperCase()) else sb.append(this[i].toLowerCase())
            false
        }
    }
    return sb.toString().trim { it <= ' ' }
}
