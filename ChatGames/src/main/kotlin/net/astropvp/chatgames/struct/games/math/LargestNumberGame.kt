package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class LargestNumberGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.LARGEST_NUMBER

    override fun generateQNA(): ActiveChatGame {
        val numbers = List((5..8).random()) { (1..75).random() }
        val largest = numbers.max() ?: 0

        val message = mutableListOf(
            "",
            "&fPick the largest number: &6${numbers.joinToString(", ")}",
            ""
        )

        return ActiveChatGame(message, largest.toString(), type)
    }
}
