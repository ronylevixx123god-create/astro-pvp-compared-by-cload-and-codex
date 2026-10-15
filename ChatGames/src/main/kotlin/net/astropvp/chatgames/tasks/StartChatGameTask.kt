package net.astropvp.chatgames.tasks

import net.astropvp.chatgames.struct.ChatGameManager
import net.astropvp.chatgames.struct.ChatGameType
import org.bukkit.scheduler.BukkitRunnable

class StartChatGameTask : BukkitRunnable() {

    override fun run() {
        if(ChatGameManager.currentGame != null) return
        val finalType = ChatGameType.pickRandom()
        val game = ChatGameManager.getGame(finalType) ?: return
        val qna = game.generateQNA()
        qna.post()
        ChatGameManager.updateGame(qna)
    }

}