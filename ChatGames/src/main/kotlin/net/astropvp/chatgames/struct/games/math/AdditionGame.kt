package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class AdditionGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.ADDITION

    override fun generateQNA(): ActiveChatGame {
        val numbers = List((2..5).random()) { (1..100).random() }
        val question = "&fSolve: &6${numbers.joinToString(" &e+ &6")}"
        val answer = numbers.sum()
        return ActiveChatGame(mutableListOf("", question, ""), answer.toString(), type)
    }
}