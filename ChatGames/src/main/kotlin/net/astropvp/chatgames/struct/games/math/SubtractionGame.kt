package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class SubtractionGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.SUBTRACTION

    override fun generateQNA(): ActiveChatGame {
        val numbers = List((2..3).random()) { (100..1000).random() }.sortedDescending()
        val question = "&fSolve: &6${numbers.joinToString(" &e- &6")}"

        val answer = when (numbers.size) {
            2 -> numbers[0] - numbers[1]
            3 -> {
                numbers[0] - numbers[1] - numbers[2]
            }
            else -> 0
        }

        return ActiveChatGame(mutableListOf("", question, ""), answer.toString(), type)
    }

}