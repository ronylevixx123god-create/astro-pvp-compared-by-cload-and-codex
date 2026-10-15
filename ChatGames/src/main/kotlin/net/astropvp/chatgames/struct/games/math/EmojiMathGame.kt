package net.astropvp.chatgames.struct.games.math
import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class EmojiMathGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.EMOJI_MATH

    override fun generateQNA(): ActiveChatGame {
        val numbers = List((2..5).random()) { (1..4).random() }
        val emojis = mapOf(
            "✦" to (1..4).random(),
            "❥" to (5..9).random(),
            "♡" to (10..15).random(),
            "ꕥ" to (16..20).random()
        )
        val emojiMap = emojis.keys.shuffled().take(numbers.size).zip(numbers).toMap()
        val message = mutableListOf("").apply {
            emojiMap.forEach { add("&f• ${it.key} &8- &6${emojis[it.key]}") }
            add("&fSolve: &6${emojiMap.map { it.key.repeat(it.value) }.joinToString(" &e+ &6")}")
            add("")
        }
        val answer = emojiMap.entries.sumBy { (emoji, count) -> (emojis[emoji] ?: 1) * count }
        return ActiveChatGame(message, answer.toString(), type)
    }

}
