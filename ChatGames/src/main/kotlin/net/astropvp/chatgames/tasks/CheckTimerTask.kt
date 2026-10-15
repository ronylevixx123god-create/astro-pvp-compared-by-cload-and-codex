package net.astropvp.chatgames.tasks

import net.astropvp.chatgames.struct.ChatGameManager
import org.bukkit.scheduler.BukkitRunnable

class CheckTimerTask : BukkitRunnable() {

    override fun run() {
        val game = ChatGameManager.currentGame ?: return
        game.checkTimer()
    }

}