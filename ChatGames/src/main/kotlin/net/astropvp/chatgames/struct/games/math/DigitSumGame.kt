package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class DigitSumGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.DIGIT_SUM

    override fun generateQNA(): ActiveChatGame {
        val number = (100..999).random()
        val digitSum = number.toString().sumBy { it.toString().toInt() }

        val message = mutableListOf(
            "",
            "&fSum the digits: &6${number}",
            ""
        )

        return ActiveChatGame(message, digitSum.toString(), type)
    }
}
