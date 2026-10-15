package net.astropvp.chatgames.struct

abstract class ChatGame {

    abstract val type: ChatGameType
    abstract fun generateQNA(): ActiveChatGame

}