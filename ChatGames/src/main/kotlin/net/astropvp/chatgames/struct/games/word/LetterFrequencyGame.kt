package net.astropvp.chatgames.struct.games.word

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class LetterFrequencyGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.LETTER_FREQUENCY

    override fun generateQNA(): ActiveChatGame {
        val phrases = listOf(
            "The quick brown fox jumps over the lazy dog",
            "Pack my box with five dozen liquor jugs",
            "How razorback-jumping frogs can level six piqued gymnasts",
            "Jinxed wizards pluck ivy from the big quilt",
            "Bright vixens jump; dozy fowl quack"
        )

        val phrase = phrases.random()
        val letter = ('a'..'z').random()
        val frequency = phrase.toLowerCase().count { it == letter }

        val message = mutableListOf(
            "",
            "&fHow many times does the letter &6'$letter' &fappear in the phrase below?",
            "\"&6${phrase}\"",
            ""
        )

        return ActiveChatGame(message, frequency.toString(), type)
    }
}
