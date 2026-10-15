package net.astropvp.chatgames.struct.games.misc

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType
class RandomNumberGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.RANDOM_NUMBER

    override fun generateQNA(): ActiveChatGame {
        val number = (1..50).random()
        val question = "&fI am thinking of a number between &61 &fand &650&f, guess it!"
        return ActiveChatGame(mutableListOf("", question, ""), number.toString(), type)
    }
}