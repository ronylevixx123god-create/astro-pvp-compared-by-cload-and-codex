package net.astropvp.chatgames.struct

import net.astropvp.chatgames.utils.capitalCase
import net.astropvp.chatgames.utils.msg
import org.bukkit.Bukkit

class ActiveChatGame(val question: MutableList<String>, val answer: String, val type: ChatGameType) {

    private val time = 45

    private val startTime: Long = System.currentTimeMillis()

    fun post() {
        if(checkTimer()) return

        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            onlinePlayer.msg("")
            onlinePlayer.msg("&6&lChat Games &8- &f${type.name.capitalCase()} Game")
            onlinePlayer.msg("&7The first person to answer the following")
            onlinePlayer.msg("&7question first will receive a slot bot ticket!")
            for (s in question) {
                onlinePlayer.msg(s)
            }
            onlinePlayer.msg("&7Type your answer in chat, but be quick!")
        }
    }

    fun checkTimer(): Boolean {
        if(System.currentTimeMillis() > startTime + (time * 1000)) {
            cancel()
            return true
        }
        return false
    }

    private fun cancel() {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            onlinePlayer.msg("&c&lChat Games &8- &fGame Failure!")
            for (s in question) {
                onlinePlayer.msg(s
                    .replace("&6", "&c")
                    .replace("&e", "&4")
                )
            }
            onlinePlayer.msg("&cNobody got the answer in time, it was $answer")
        }
        ChatGameManager.currentGame = null
    }
}