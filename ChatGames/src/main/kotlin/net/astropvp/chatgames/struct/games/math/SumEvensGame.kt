package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class SumEvensGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.SUM_EVEN_NUMBERS

    override fun generateQNA(): ActiveChatGame {
        val limit = (10..50).random()
        val sumOfEvens = (2..limit step 2).sum()

        val message = mutableListOf(
            "",
            "&fSum of all even numbers between &61 &fand &6$limit",
            ""
        )

        return ActiveChatGame(message, sumOfEvens.toString(), type)
    }
}
