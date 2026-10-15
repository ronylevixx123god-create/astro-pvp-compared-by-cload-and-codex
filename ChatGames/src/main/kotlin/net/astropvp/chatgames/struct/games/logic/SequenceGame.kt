package net.astropvp.chatgames.struct.games.logic

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class SequenceGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.SEQUENCE

    override fun generateQNA(): ActiveChatGame {
        val start = (1..10).random()
        val diff = (1..5).random()
        val length = 4
        val sequence = List(length) { start + it * diff }
        val question = "&fComplete the sequence: &6${sequence.joinToString("&e, &6")} &e+ &6?"
        val answer = (start + length * diff).toString()
        return ActiveChatGame(mutableListOf("", question, ""), answer, type)
    }
}
