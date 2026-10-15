package net.astropvp.chatgames

import me.mattstudios.mf.base.CommandManager
import me.mattstudios.mf.base.components.TypeResult
import net.astropvp.chatgames.commands.ChatGameCommand
import net.astropvp.chatgames.listeners.ChatGameListener
import net.astropvp.chatgames.struct.ChatGameRegister
import net.astropvp.chatgames.struct.ChatGameType
import net.astropvp.chatgames.tasks.CheckTimerTask
import net.astropvp.chatgames.tasks.StartChatGameTask
import org.bukkit.plugin.java.JavaPlugin

class ChatGamesPlugin : JavaPlugin() {

    private lateinit var commandManager: CommandManager

    init {
        instance = this
    }

    companion object {
        lateinit var instance: ChatGamesPlugin
    }

    override fun onEnable() {
        this.commandManager = CommandManager(this)
        registerCommandParameters()
        commandManager.register(ChatGameCommand())

        val startChatGameTask = StartChatGameTask()
        startChatGameTask.runTaskTimer(this, 15 * 60 * 20L, 5 * 60 * 20L)

        val checkTimerTask = CheckTimerTask()
        checkTimerTask.runTaskTimer(this, 30 * 20L, 30 * 20L)

        ChatGameRegister.register()

        registerListeners()
    }

    private fun registerListeners() {
        val pm = server.pluginManager
        pm.registerEvents(ChatGameListener(), this)
    }


    override fun onDisable() {

    }

    private fun registerCommandParameters() {
        this.commandManager.parameterHandler.register(ChatGameType::class.java) { argument: Any ->
            val chatGameType: ChatGameType = ChatGameType.valueOf(argument.toString().toUpperCase())
            TypeResult(chatGameType, argument)
        }

    }
}