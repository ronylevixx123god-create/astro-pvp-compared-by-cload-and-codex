package net.astropvp.chatgames.struct

import net.astropvp.chatgames.struct.games.logic.ReverseNumberGame
import net.astropvp.chatgames.struct.games.logic.RiddleGame
import net.astropvp.chatgames.struct.games.logic.SequenceGame
import net.astropvp.chatgames.struct.games.logic.TriviaGame
import net.astropvp.chatgames.struct.games.math.*
import net.astropvp.chatgames.struct.games.math.AdditionGame
import net.astropvp.chatgames.struct.games.misc.DaysInMonthGame
import net.astropvp.chatgames.struct.games.misc.RandomNumberGame
import net.astropvp.chatgames.struct.games.word.*

object ChatGameRegister {

    fun register() {
        ChatGameManager.register(ChatGameType.ADDITION, AdditionGame())
        ChatGameManager.register(ChatGameType.SUBTRACTION, SubtractionGame())
        ChatGameManager.register(ChatGameType.UNSCRAMBLE, ScrambleGame())
        ChatGameManager.register(ChatGameType.MULTIPLICATION, MultiplicationGame())
        ChatGameManager.register(ChatGameType.DIVISION, DivisionGame())
        ChatGameManager.register(ChatGameType.SEQUENCE, SequenceGame())
        ChatGameManager.register(ChatGameType.TRIVIA, TriviaGame())
        ChatGameManager.register(ChatGameType.RIDDLE, RiddleGame())
        ChatGameManager.register(ChatGameType.EMOJI_MATH, EmojiMathGame())
        ChatGameManager.register(ChatGameType.REVERSE_MATH, ReverseMathGame())
        ChatGameManager.register(ChatGameType.PERIMETER, PerimeterGame())
        ChatGameManager.register(ChatGameType.REVERSE_NUMBER, ReverseNumberGame())
        ChatGameManager.register(ChatGameType.LETTER_FREQUENCY, LetterFrequencyGame())
        ChatGameManager.register(ChatGameType.DIGIT_SUM, DigitSumGame())
        ChatGameManager.register(ChatGameType.ANGLE_SUM, AngleSumGame())
        ChatGameManager.register(ChatGameType.WORD_LENGTH, WordLengthGame())
        ChatGameManager.register(ChatGameType.SIMPLIFY_FRACTION, SimpleFractionGame())
        ChatGameManager.register(ChatGameType.SQUARE_ROOT, SquareRootGame())
        ChatGameManager.register(ChatGameType.RANDOM_NUMBER, RandomNumberGame())
        ChatGameManager.register(ChatGameType.FACTORIAL, FactorialGame())
        ChatGameManager.register(ChatGameType.LARGEST_NUMBER, LargestNumberGame())
        ChatGameManager.register(ChatGameType.COUNT_VOWELS, LargestNumberGame())
        ChatGameManager.register(ChatGameType.DAYS_IN_MONTH, DaysInMonthGame())
        ChatGameManager.register(ChatGameType.ALPHABET_POSITION, AlphabetPositionGame())
        ChatGameManager.register(ChatGameType.MEAN, MeanGame())
        ChatGameManager.register(ChatGameType.MEDIAN, MedianGame())
        ChatGameManager.register(ChatGameType.SUM_EVEN_NUMBERS, SumEvensGame())
        ChatGameManager.register(ChatGameType.SMALLEST_NUMBER, SmallestNumberGame())
        ChatGameManager.register(ChatGameType.LONGEST_WORD, LongestWordGame())
    }

}