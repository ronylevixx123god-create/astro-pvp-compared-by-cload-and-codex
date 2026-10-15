package net.astropvp.chatgames.struct.games.logic

import net.astropvp.chatgames.struct.ActiveChatGame
import net.astropvp.chatgames.struct.ChatGame
import net.astropvp.chatgames.struct.ChatGameType

class TriviaGame : ChatGame() {
    override val type: ChatGameType = ChatGameType.TRIVIA

    override fun generateQNA(): ActiveChatGame {
        val trivias = listOf(
            "What planet is known as the Red Planet?" to "mars",
            "What is the largest mammal in the world?" to "whale",
            "What element does 'O' represent on the periodic table?" to "oxygen",
            "What is the hardest natural substance on Earth?" to "diamond",
            "Which ocean is the largest in the world?" to "pacific",
            "In which year did the Titanic sink?" to "1912",
            "Who painted the Mona Lisa?" to "leonardo da vinci",
            "What is the capital of France?" to "paris",
            "How many continents are there on Earth?" to "seven",
            "What is the smallest prime number?" to "2",
            "What is the chemical symbol for gold?" to "au",
            "Which planet is known as the Earth's twin?" to "venus",
            "Who discovered penicillin?" to "fleming",
            "What is the main ingredient in guacamole?" to "avocado",
            "What is the longest river in the world?" to "nile",
            "Which country is known as the Land of the Rising Sun?" to "japan",
            "What is the capital city of Australia?" to "canberra",
            "What is the largest island in the world?" to "greenland",
            "Which element has the chemical symbol 'Fe'?" to "iron",
            "What is the tallest mountain in the world?" to "everest",
            "What is the capital of Canada?" to "ottawa",
            "What is the largest desert in the world?" to "sahara",
            "Which planet is known as the 'Giant Planet'?" to "jupiter",
            "What is the most abundant gas in Earth's atmosphere?" to "nitrogen",
            "What is the smallest country in the world?" to "vatican city",
            "What is the longest river in Asia?" to "yangtze",
            "What is the name of the galaxy we live in?" to "milkyway",
            "Which fruit is known for having its seeds on the outside?" to "strawberry",
            "What is the chemical symbol for silver?" to "ag",
            "Which planet is known for its rings?" to "saturn",
            "What is the capital of Italy?" to "rome",
            "What animal is known as the 'King of the Jungle'?" to "lion"
        )


        val (trivia, answer) = trivias.random()
        val question = "&fTrivia: &6$trivia"
        return ActiveChatGame(mutableListOf("", question, ""), answer, type)
    }
}