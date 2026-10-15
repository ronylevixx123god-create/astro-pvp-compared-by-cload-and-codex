package net.astropvp.chatgames.struct.games.word

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameHelper
import net.astropvp.chatgames.struct.ChatGameType
class ScrambleGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.UNSCRAMBLE

    override fun generateQNA(): ActiveChatGame {
        val word = pickRandomWord()
        val scrambled = word.toList().shuffled().joinToString("")
        val question = "&fUnscramble the word: &6${scrambled}"
        return ActiveChatGame(mutableListOf("", question, ""), word, type)
    }

    private fun pickRandomWord(): String {
        return ChatGameHelper.words.random()
    }
}