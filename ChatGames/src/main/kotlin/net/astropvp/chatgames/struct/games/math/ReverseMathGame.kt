package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class ReverseMathGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.REVERSE_MATH

    override fun generateQNA(): ActiveChatGame {
        val numbers = List(2) { (1..10).random() }
        val (num1, num2) = numbers
        val operations = listOf("+", "-", "*", "/")
        val operation = operations.random()

        val question: String
        val answer: Int

        when (operation) {
            "+" -> {
                question = "$num1 + ? = $num2"
                answer = num2 - num1
            }
            "-" -> {
                question = "$num1 - ? = $num2"
                answer = num1 - num2
            }
            "*" -> {
                question = "$num1 * ? = $num2"
                answer = num2 / num1
            }
            "/" -> {
                question = "$num1 / ? = $num2"
                answer = num1 / num2
            }
            else -> throw IllegalStateException("Unexpected operation: $operation")
        }

        val message = mutableListOf(
            "",
            "&fSolve the equation: &6$question",
            ""
        )
        return ActiveChatGame(message, answer.toString(), type)
    }
}

