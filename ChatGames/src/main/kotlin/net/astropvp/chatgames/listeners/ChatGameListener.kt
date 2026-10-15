package net.astropvp.chatgames.listeners

import net.astropvp.chatgames.struct.ChatGameManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatGameListener : Listener {

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        if(checkChatGame(event)) {
            event.isCancelled = true
            ChatGameManager.giveReward(event.player)
            return
        }
    }

    private fun checkChatGame(event: AsyncPlayerChatEvent): Boolean {
        if(ChatGameManager.currentGame == null) return false
        val game = ChatGameManager.currentGame!!
        return event.message.toLowerCase() == (game.answer.toLowerCase())
    }

}