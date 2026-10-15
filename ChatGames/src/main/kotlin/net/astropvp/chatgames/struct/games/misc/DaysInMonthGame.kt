package net.astropvp.chatgames.struct.games.misc

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType
import java.time.Month
import java.time.YearMonth

class DaysInMonthGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.DAYS_IN_MONTH

    override fun generateQNA(): ActiveChatGame {
        val year = (2000..2024).random()
        val month = Month.values().random()
        val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

        val message = mutableListOf(
            "",
            "&fHow many days are in &6${month.name}, $year",
            ""
        )

        return ActiveChatGame(message, daysInMonth.toString(), type)
    }
}
