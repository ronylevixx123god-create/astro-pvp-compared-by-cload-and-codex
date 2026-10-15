package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType
class DivisionGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.DIVISION

    override fun generateQNA(): ActiveChatGame {
        var numbers: List<Int>
        var answer: Int

        do {
            numbers = List(2) { if (it == 1) (1..100).random() else 0 }
            numbers = numbers.toMutableList().apply {
                this[0] = (1..100).random() * this[1]
            }
            answer = numbers[0] / numbers[1]
        } while (numbers[0] <= numbers[1] || numbers[0] % numbers[1] != 0)

        val question = "&fSolve: &6${numbers.joinToString(" &e/ &6")}"
        return ActiveChatGame(mutableListOf("", question, ""), answer.toString(), type)
    }
}