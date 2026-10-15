package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType
class AngleSumGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.ANGLE_SUM

    override fun generateQNA(): ActiveChatGame {
        val sides = (3..10).random()
        val angleSum = (sides - 2) * 180

        val message = mutableListOf(
            "",
            "&fSum of the &6interior angles &fof a shape with &6$sides sides",
            ""
        )

        return ActiveChatGame(message, angleSum.toString(), type)
    }
}
