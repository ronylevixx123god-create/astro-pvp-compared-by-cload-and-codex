package net.astropvp.chatgames.struct

import me.ily2bb.slotbot.CosmicSlotBotAPI
import me.ily2bb.slotbot.managers.SlotBotManager
import net.astropvp.chatgames.utils.sendToAllOnlinePlayers
import org.bukkit.entity.Player

object ChatGameManager {

    var currentGame: ActiveChatGame? = null

    private val games: MutableMap<ChatGameType, ChatGame> = mutableMapOf()

    fun getGame(game: ChatGameType): ChatGame? {
        return games[game]
    }

    fun updateGame(qna: ActiveChatGame) {
        currentGame = qna
    }

    fun register(chatGameType: ChatGameType, game: ChatGame) {
        games[chatGameType] = game
    }

    fun giveReward(player: Player) {
        val slotbot = CosmicSlotBotAPI.createSlotBotItem()
        slotbot.amount = 4
        player.inventory.addItem(slotbot)
        announceReward(player)
    }

    private fun announceReward(player: Player) {
        "&6&lChat Games &8➥ &6${player.name} &fhas guessed the answer &6${currentGame?.answer} &ffirst!".sendToAllOnlinePlayers()
        "&6&lChat Games &8➥ &fThey have won 4x &6&lAstro &f&lSlot Bot &6&lTicket".sendToAllOnlinePlayers()
        reset()
    }

    private fun reset() {
        currentGame = null
    }
}