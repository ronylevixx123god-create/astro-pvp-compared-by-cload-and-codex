package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class MedianGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.MEDIAN

    override fun generateQNA(): ActiveChatGame {
        val numbers = List((4..9).random()) { (1..20).random() }
        val sortedNumbers = numbers.sorted()
        val median = if (sortedNumbers.size % 2 == 1) {
            sortedNumbers[sortedNumbers.size / 2].toDouble()
        } else {
            (sortedNumbers[sortedNumbers.size / 2 - 1] + sortedNumbers[sortedNumbers.size / 2]) / 2.0
        }

        val message = mutableListOf(
            "",
            "&fFind the median: &6${numbers.joinToString(", ")} &f(1 d.p)",
            ""
        )

        val result = "%.1f".format(median)
        val finalResult = if (result.endsWith(".0")) {
            result.removeSuffix(".0")
        } else {
            result
        }
        return ActiveChatGame(message, finalResult, type)
    }
}
