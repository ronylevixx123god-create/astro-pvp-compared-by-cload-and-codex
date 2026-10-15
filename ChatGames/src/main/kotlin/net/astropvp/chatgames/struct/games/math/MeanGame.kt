package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class MeanGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.MEAN

    override fun generateQNA(): ActiveChatGame {
        val numbers = List(5) { (1..20).random() }
        val mean = numbers.average()
        val message = mutableListOf(
            "",
            "&fFind the mean: &6${numbers.joinToString(", ")} &f(1 d.p)",
            ""
        )

        return ActiveChatGame(message, "%.1f".format(mean), type)
    }
}