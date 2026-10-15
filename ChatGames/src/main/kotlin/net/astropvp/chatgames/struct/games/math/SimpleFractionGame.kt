package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class SimpleFractionGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.SIMPLIFY_FRACTION

    override fun generateQNA(): ActiveChatGame {
        val numerator = (1..20).random()
        val denominator = (numerator..20).random()  // Ensure the denominator is >= numerator

        val gcd = gcd(numerator, denominator)
        val simplifiedNumerator = numerator / gcd
        val simplifiedDenominator = denominator / gcd

        val message = mutableListOf(
            "",
            "&fSimplify this fraction: &6$numerator/$denominator",
            ""
        )

        return ActiveChatGame(message, "$simplifiedNumerator/$simplifiedDenominator", type)
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }
}
