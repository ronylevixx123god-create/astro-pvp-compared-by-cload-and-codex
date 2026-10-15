package net.astropvp.chatgames.struct.games.word

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameHelper.words
import net.astropvp.chatgames.struct.ChatGameType

class CountVowelsGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.COUNT_VOWELS

    override fun generateQNA(): ActiveChatGame {
        val word = words.random()
        val vowels = "aeiou"
        val vowelCount = word.count { it in vowels }

        val message = mutableListOf(
            "",
            "&fCount the vowels in: &6$word",
            ""
        )

        return ActiveChatGame(message, vowelCount.toString(), type)
    }
}
