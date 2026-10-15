package net.astropvp.chatgames.struct.games.word

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType


class AlphabetPositionGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.ALPHABET_POSITION

    override fun generateQNA(): ActiveChatGame {
        val letter = ('A'..'Z').random()
        val position = letter - 'A' + 1

        val message = mutableListOf(
            "",
            "&fWhat position is the letter &6${letter} &fin the alphabet",
            ""
        )

        return ActiveChatGame(message, position.toString(), type)
    }
}
