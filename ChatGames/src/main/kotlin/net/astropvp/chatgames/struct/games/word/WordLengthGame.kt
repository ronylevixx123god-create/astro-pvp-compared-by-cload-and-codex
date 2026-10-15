package net.astropvp.chatgames.struct.games.word

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameHelper
import net.astropvp.chatgames.struct.ChatGameType

class WordLengthGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.WORD_LENGTH

    override fun generateQNA(): ActiveChatGame {
        val word = ChatGameHelper.words.random()
        val length = word.length

        val message = mutableListOf(
            "",
            "&fWhat is the length of this word: &6$word",
            ""
        )

        return ActiveChatGame(message, length.toString(), type)
    }
}
