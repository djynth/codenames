package core

/**
 * Represents a single card on the board.
 * If the team of the card is not visible in the current context, it will be null.
 */
data class Card(val word: String, val team: Team?, val revealed: Boolean) {
    companion object {
        /**
         * Checks whether two words are semantically equal (i.e. equal ignoring surrounding
         *  whitespace and case).
         */
        fun wordsEqual(word1: String, word2: String): Boolean {
            return word1.trim().equals(word2.trim(), true)
        }
    }
}
