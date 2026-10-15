package net.astropvp.chatgames.utils

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PermissionUtils {

    fun checkPermission(player: Player, permission: String): Boolean {
        if(player.hasPermission(permission)) return true
        player.msg("<error-prefix> <red>You do not have permission to execute this command")
        player.playErrorSound()
        return false
    }

    fun checkPermission(sender: CommandSender, permission: String): Boolean {
        if(sender.hasPermission(permission)) return true
        sender.msg("<error-prefix> <red>You do not have permission to execute this command")
        if(sender is Player) {
            sender.playErrorSound()
        }
        return false
    }

}