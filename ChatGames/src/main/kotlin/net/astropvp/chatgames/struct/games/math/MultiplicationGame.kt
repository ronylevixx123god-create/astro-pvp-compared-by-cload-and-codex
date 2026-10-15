package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType
class MultiplicationGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.MULTIPLICATION

    override fun generateQNA(): ActiveChatGame {
        val numbers = List(2) { (1..100).random() }
        val question = "&fSolve: &6${numbers.joinToString(" &ex &6")}"
        val answer = numbers[0] * numbers[1]
        return ActiveChatGame(mutableListOf("", question, ""), answer.toString(), type)
    }
}