package net.astropvp.chatgames.struct.games.word

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameHelper
import net.astropvp.chatgames.struct.ChatGameType


class LongestWordGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.LONGEST_WORD

    override fun generateQNA(): ActiveChatGame {
        val words = List((4..6).random()) { ChatGameHelper.words.random() }
        val longest = words.maxBy { it.length } ?: ""

        val message = mutableListOf(
            "",
            "&fWhich word is the longest? &6${words}",
            ""
        )

        return ActiveChatGame(message, longest, type)
    }
}
