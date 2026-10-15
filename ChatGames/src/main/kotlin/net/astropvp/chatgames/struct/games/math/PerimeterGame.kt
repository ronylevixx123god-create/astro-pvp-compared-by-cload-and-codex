package net.astropvp.chatgames.struct.games.math

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class PerimeterGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.PERIMETER

    override fun generateQNA(): ActiveChatGame {
        val shapes = listOf("Triangle", "Cuboid", "Pentagon", "Hexagon", "Heptagon", "Octagon")
        val shape = shapes.random()
        val sides = when (shape) {
            "Triangle" -> 3
            "Cuboid" -> 4
            "Pentagon" -> 5
            "Hexagon" -> 6
            "Heptagon" -> 7
            "Octagon" -> 8
            else -> 0
        }
        val sideLengths = List(sides) { (1..10).random() }
        val perimeter = sideLengths.sum()

        val message = mutableListOf(
            "",
            "&fCalculate the perimeter of this &6$shape:",
            "&fSide Lengths: &6${sideLengths.joinToString(", ")}",
            ""
        )

        return ActiveChatGame(message, perimeter.toString(), type)
    }
}
