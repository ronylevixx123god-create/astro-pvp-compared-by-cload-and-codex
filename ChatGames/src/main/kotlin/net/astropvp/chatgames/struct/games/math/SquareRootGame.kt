package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class SquareRootGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.SQUARE_ROOT

    override fun generateQNA(): ActiveChatGame {
        val answer = (1..20).random()  // Generate a perfect square
        val question = answer * answer

        val message = mutableListOf(
            "",
            "&fSquare Root of: &6$question",
            ""
        )

        return ActiveChatGame(message, answer.toString(), type)
    }
}
