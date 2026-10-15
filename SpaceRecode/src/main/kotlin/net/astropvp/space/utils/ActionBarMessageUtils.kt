package net.astropvp.space.utils

import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

object ActionBarMessageUtils {

    fun sendMessage(p: Player, message: String) {
        val packet = PacketPlayOutChat(
            IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"),
            2.toByte()
        )
        (p as CraftPlayer).handle.playerConnection.sendPacket(packet)
    }

    fun broadcastMessage(message: String) {
        val packet = PacketPlayOutChat(
            IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"),
            2.toByte()
        )
        for (p in Bukkit.getServer().onlinePlayers) {
            (p as CraftPlayer).handle.playerConnection.sendPacket(packet)
        }
    }
}