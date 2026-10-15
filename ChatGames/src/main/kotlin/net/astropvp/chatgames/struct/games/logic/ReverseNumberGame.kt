package net.astropvp.chatgames.struct.games.logic

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class ReverseNumberGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.REVERSE_NUMBER

    override fun generateQNA(): ActiveChatGame {
        val length = (7..10).random()
        val number = generateRandomNumber(length)
        val reversedNumber = number.reversed()

        val message = mutableListOf(
            "",
            "&fReverse the number: &6$number",
            ""
        )
        return ActiveChatGame(message, reversedNumber, type)
    }

    private fun generateRandomNumber(length: Int): String {
        val random = StringBuilder()
        for (i in 0 until length) {
            random.append((0..9).random())
        }
        return random.toString()
    }
}
