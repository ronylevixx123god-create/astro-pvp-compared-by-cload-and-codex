package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class SmallestNumberGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.SMALLEST_NUMBER

    override fun generateQNA(): ActiveChatGame {
        val numbers = List((5..8).random()) { (1..75).random() }
        val largest = numbers.min() ?: 0

        val message = mutableListOf(
            "",
            "&fPick the smallest number: &6${numbers.joinToString(", ")}",
            ""
        )

        return ActiveChatGame(message, largest.toString(), type)
    }
}
