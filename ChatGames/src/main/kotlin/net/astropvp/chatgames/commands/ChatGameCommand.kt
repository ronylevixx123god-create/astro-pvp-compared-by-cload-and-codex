package net.astropvp.chatgames.commands

import me.mattstudios.mf.annotations.Alias
import me.mattstudios.mf.annotations.Command
import me.mattstudios.mf.annotations.Default
import me.mattstudios.mf.annotations.SubCommand
import me.mattstudios.mf.base.CommandBase
import net.astropvp.chatgames.struct.ChatGameManager
import net.astropvp.chatgames.struct.ChatGameType
import net.astropvp.chatgames.utils.PermissionUtils
import net.astropvp.chatgames.utils.Permissions
import net.astropvp.chatgames.utils.msg
import org.bukkit.command.CommandSender

@Command("chatgame")
@Alias("chatgames")
class ChatGameCommand : CommandBase() {

    @Default
    fun defaultCommand(sender: CommandSender) {
        help(sender)
        println(TestClass.test())
    }

    @SubCommand("help")
    fun helpCommand(sender: CommandSender) {
        help(sender)
    }

    @SubCommand("startrandom")
    fun startRandomCommand(sender: CommandSender) {
        if(!PermissionUtils.checkPermission(sender, Permissions.CHAT_GAMES_ADMIN)) return
        val game = ChatGameManager.getGame(ChatGameType.pickRandom()) ?: return
        val qna = game.generateQNA()
        qna.post()
        ChatGameManager.updateGame(qna)
    }

    @SubCommand("start")
    fun startCommand(sender: CommandSender, type: ChatGameType) {
        if(!PermissionUtils.checkPermission(sender, Permissions.CHAT_GAMES_ADMIN)) return
        val game = ChatGameManager.getGame(type) ?: return
        val qna = game.generateQNA()
        qna.post()
        ChatGameManager.updateGame(qna)
    }

    @SubCommand("end")
    fun endCommand(sender: CommandSender) {
        if(!PermissionUtils.checkPermission(sender, Permissions.CHAT_GAMES_ADMIN)) return
        ChatGameManager.currentGame = null
    }


    private fun help(sender: CommandSender) {
        if(!PermissionUtils.checkPermission(sender, Permissions.CHAT_GAMES_ADMIN)) return
        sender.msg("&6Chatgame help")
        sender.msg("&6/chatgame startrandom")
        sender.msg("&6/chatgame start <chat-game>")
        sender.msg("&6/chatgame end")
        sender.msg("")
    }
}