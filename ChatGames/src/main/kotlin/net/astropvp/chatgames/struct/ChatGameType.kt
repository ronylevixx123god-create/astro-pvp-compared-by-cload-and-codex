package net.astropvp.chatgames.struct

enum class ChatGameType {
    ADDITION,
    SUBTRACTION,
    DIVISION,
    MULTIPLICATION,
    UNSCRAMBLE,
    EMOJI_MATH,
    RIDDLE,
    TRIVIA,
    SEQUENCE,
    REVERSE_MATH,
    PERIMETER,
    REVERSE_NUMBER,
    LETTER_FREQUENCY,
    DIGIT_SUM,
    ANGLE_SUM,
    WORD_LENGTH,
    SIMPLIFY_FRACTION,
    SQUARE_ROOT,
    RANDOM_NUMBER,
    SMALLEST_NUMBER,
    FACTORIAL,
    LARGEST_NUMBER,
    COUNT_VOWELS,
    DAYS_IN_MONTH,
    ALPHABET_POSITION,
    MEAN,
    MEDIAN,
    SUM_EVEN_NUMBERS,
    LONGEST_WORD,
    ;

    companion object {
        fun pickRandom(): ChatGameType {
            return ChatGameType.values().random()
        }
    }
}