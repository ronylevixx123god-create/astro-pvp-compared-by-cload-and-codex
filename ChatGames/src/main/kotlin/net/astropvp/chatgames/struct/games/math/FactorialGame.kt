package net.astropvp.chatgames.struct.games.math
import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class FactorialGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.FACTORIAL

    override fun generateQNA(): ActiveChatGame {
        val number = (3..6).random()
        val factorial = (1..number).reduce { acc, i -> acc * i }

        val message = mutableListOf(
            "",
            "&fCalculate factorial of &6$number",
            ""
        )

        return ActiveChatGame(message, factorial.toString(), type)
    }
}
