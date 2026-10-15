package net.astropvp.chatgames.struct.games.logic

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType
class RiddleGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.RIDDLE

    override fun generateQNA(): ActiveChatGame {
        val riddles = listOf(
            "I speak without a mouth and hear without ears. What am I?" to "echo",
            "What has keys but can't open locks?" to "piano",
            "What can travel around the world while staying in a corner?" to "stamp",
            "What has a heart that doesn’t beat?" to "artichoke",
            "What has to be broken before you can use it?" to "egg",
            "What has hands but can’t clap?" to "clock",
            "What gets wetter the more it dries?" to "towel",
            "What can you catch but not throw?" to "cold",
            "What goes up but never comes down?" to "age",
            "What has a neck but no head?" to "bottle",
            "What can be cracked, made, told, and played?" to "joke",
            "What has cities, but no houses; forests, but no trees; and rivers, but no water?" to "map",
            "What runs but never walks, has a bed but never sleeps?" to "river",
            "What can you hold in your right hand but not in your left?" to "left hand",
            "What has one eye but can’t see?" to "needle",
            "What is full of holes but still holds water?" to "sponge",
            "What is so fragile that saying its name breaks it?" to "silence",
            "What comes once in a minute, twice in a moment, but never in a thousand years?" to "m",
            "What is always in front of you but can’t be seen?" to "future",
            "What can be broken without being touched?" to "promise",
            "What has a thumb and four fingers but is not alive?" to "glove",
            "What begins and has no end, and is the key to every door?" to "circle",
            "What has a bed but doesn’t sleep?" to "river",
            "What has a face and two hands but no arms or legs?" to "clock",
            "What is made of water but if you put it into water it will die?" to "ice",
            "What is at the end of a rainbow?" to "W",
            "What can be heard but not seen, and gets louder when you are alone?" to "echo",
            "What has many keys but can’t open a single door?" to "piano",
            "What can be broken by just speaking?" to "silence",
            "What can travel around the world but stays in one corner?" to "stamp",
            "What starts with an E, ends with an E, but only has one letter?" to "envelope",
            "What goes through cities and fields but never moves?" to "road"
        )

        val (riddle, answer) = riddles.random()
        val question = "&fRiddle: &6$riddle"
        return ActiveChatGame(mutableListOf("", question, ""), answer, type)
    }
}